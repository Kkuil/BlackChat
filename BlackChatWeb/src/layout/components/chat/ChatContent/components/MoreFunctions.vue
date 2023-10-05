<script setup lang="ts">
import { ref } from "vue"
import {
    ElMessage,
    ElMessageBox,
    UploadFile,
    UploadInstance,
    UploadRawFile
} from "element-plus"
import { convertImageToBase64, getImageSize } from "@/utils/imageUtil"
import { MessageTypeEnum } from "@/enums/MessageTypeEnum"
import { upload, uploadFile, uploadVideo } from "@/api/upload"
import { useMessageStore } from "@/stores/message"
import { sendMessage } from "@/api/chat"

defineProps<{
    activeMoreFunction: boolean
    close: () => void
}>()

const moreFunctionRef = ref<HTMLDivElement>()
const messageStore = useMessageStore()

const imageUploadRef = ref<UploadInstance>()
const fileUploadRef = ref<UploadInstance>()
const videoUploadRef = ref<UploadInstance>()

const UPLOAD_CONFIG = {
    IMAGE: {
        type: ["image/png", "image/jpeg", "image/gif", "image/webp"],
        maxSize: 2 * 1024 * 1024,
        handleImageChange: async (file: UploadFile) => {
            imageUploadRef.value?.clearFiles()
            if (
                UPLOAD_CONFIG.IMAGE.type.indexOf(file.raw?.type as string) == -1
            ) {
                ElMessage.error("只允许上传图片类型，请重新选择")
                return false
            } else if (file.raw?.size / UPLOAD_CONFIG.IMAGE.maxSize > 1) {
                ElMessage.error("图片大小不能超过2MB，请重新选择")
                return false
            }
            const base64 = await convertImageToBase64(file.raw)
            ElMessageBox.confirm(
                `<div class="h-[150px] flex-center"><img src="${base64}" class="h-full" alt="" /></div>`,
                "是否发送该图片？",
                {
                    dangerouslyUseHTMLString: true,
                    distinguishCancelAndClose: true,
                    confirmButtonText: "发送",
                    cancelButtonText: "取消"
                }
            )
                .then(async () => {
                    // 上传图片
                    const uploadResult = await upload(file.raw as UploadRawFile)
                    if (uploadResult.data) {
                        // 上传成功
                        // 获取图片大小
                        const size = await getImageSize(
                            file.raw as File,
                            uploadResult.data
                        )
                        // 将上传类型改为图片
                        const imageBody: ChatMessageReq.ImageMessageBody = {
                            size: file.size as number,
                            width: size.width,
                            height: size.height,
                            url: uploadResult.data
                        }
                        messageStore.updateMessageType(
                            MessageTypeEnum.IMAGE,
                            imageBody
                        )
                        // 发送消息
                        const result = await sendMessage(
                            messageStore.messageInfo
                        )
                        if (result.data) {
                            messageStore.addMessage(result.data)
                        }
                    }
                })
                .catch(() => {
                    console.log("close")
                })
        }
    },
    FILE: {
        type: ["all"],
        maxSize: 5 * 1024 * 1024,
        handleFileChange: async (file: UploadFile) => {
            fileUploadRef.value?.clearFiles()
            if (file.raw?.size / UPLOAD_CONFIG.FILE.maxSize > 1) {
                ElMessage.error("文件大小不能超过5MB，请重新选择")
                return false
            }
            ElMessageBox.confirm("", "是否发送该文件？", {
                dangerouslyUseHTMLString: true,
                distinguishCancelAndClose: true,
                confirmButtonText: "发送",
                cancelButtonText: "取消"
            })
                .then(async () => {
                    // 上传文件
                    const uploadResult = await uploadFile(
                        file.raw as UploadRawFile
                    )
                    if (uploadResult.data) {
                        // 上传成功
                        // 将上传类型改为文件
                        const fileBody: ChatMessageReq.FileMessageBody = {
                            size: file.size as number,
                            fileName: file.name,
                            url: uploadResult.data
                        }
                        messageStore.updateMessageType(
                            MessageTypeEnum.FILE,
                            fileBody
                        )
                        // 发送消息
                        const result = await sendMessage(
                            messageStore.messageInfo
                        )
                        if (result.data) {
                            messageStore.addMessage(result.data)
                        }
                    }
                })
                .catch(() => {
                    console.log("close")
                })
        }
    },
    VIDEO: {
        type: [
            "avi",
            "mp4",
            "mov",
            "wmv",
            "flv",
            "mkv",
            "mpeg",
            "3gp",
            "vob",
            "ts",
            "m4v",
            "webm",
            "rm",
            "swf",
            "mpg"
        ],
        maxSize: 100 * 1024 * 1024,
        handleVideoChange: async (file: UploadFile) => {
            videoUploadRef.value?.clearFiles()
            if (
                UPLOAD_CONFIG.VIDEO.type.indexOf(
                    file.raw?.name.split(".")[1]
                ) == -1
            ) {
                ElMessage.error("只允许上传视频类型，请重新选择")
                return false
            } else if (file.raw?.size / UPLOAD_CONFIG.VIDEO.maxSize > 1) {
                ElMessage.error("视频文件大小不能超过100MB，请重新选择")
                return false
            }
            ElMessageBox.confirm("", "是否发送该视频？", {
                dangerouslyUseHTMLString: true,
                distinguishCancelAndClose: true,
                confirmButtonText: "发送",
                cancelButtonText: "取消"
            })
                .then(async () => {
                    // 上传文件
                    const uploadResult = await uploadVideo(
                        file.raw as UploadRawFile
                    )
                    if (uploadResult.data) {
                        // 上传成功
                        // 将上传类型改为文件
                        const videoBody: ChatMessageReq.VideoMessageBody = {
                            size: file.size as number,
                            videoName: file.name,
                            url: uploadResult.data
                        }
                        messageStore.updateMessageType(
                            MessageTypeEnum.VIDEO,
                            videoBody
                        )
                        // 发送消息
                        const result = await sendMessage(
                            messageStore.messageInfo
                        )
                        if (result.data) {
                            messageStore.addMessage(result.data)
                        }
                    }
                })
                .catch(() => {
                    console.log("close")
                })
        }
    }
}
</script>

<template>
    <div
        class="w-full more-functions bg-third rounded-b-[10px] transition-[height]"
        :class="activeMoreFunction ? 'h-[200px]' : 'h-0'"
        ref="moreFunctionRef"
        v-click-outside="
            () => {
                if (moreFunctionRef.clientHeight > 0) {
                    close()
                }
            }
        "
    >
        <div
            v-if="activeMoreFunction"
            class="w-full h-full flex justify-start p-[10px]"
        >
            <el-upload
                :limit="1"
                :auto-upload="false"
                ref="imageUploadRef"
                accept="image"
                name="file"
                :on-change="UPLOAD_CONFIG.IMAGE.handleImageChange"
                :show-file-list="false"
            >
                <template #trigger>
                    <div
                        class="flex-center flex-col w-[60px] h-[60px] cursor-pointer hover:bg-secondary mx-[5px]"
                    >
                        <SvgIcon icon-class="picture" />
                        <span class="text-[12px] mt-[5px] text-[#f5f5f5]">
                            图片
                        </span>
                    </div>
                </template>
            </el-upload>
            <el-upload
                :limit="1"
                :auto-upload="false"
                ref="fileUploadRef"
                name="file"
                :on-change="UPLOAD_CONFIG.FILE.handleFileChange"
                :show-file-list="false"
            >
                <template #trigger>
                    <div
                        class="flex-center flex-col w-[60px] h-[60px] cursor-pointer hover:bg-secondary mx-[5px]"
                    >
                        <SvgIcon icon-class="file" />
                        <span class="text-[12px] mt-[5px] text-[#f5f5f5]"
                            >文件</span
                        >
                    </div>
                </template>
            </el-upload>
            <el-upload
                :limit="1"
                :auto-upload="false"
                ref="videoUploadRef"
                name="video"
                :on-change="UPLOAD_CONFIG.VIDEO.handleVideoChange"
                :show-file-list="false"
            >
                <template #trigger>
                    <div
                        class="flex-center flex-col w-[60px] h-[60px] cursor-pointer hover:bg-secondary mx-[5px]"
                    >
                        <SvgIcon icon-class="video" />
                        <span class="text-[12px] mt-[5px] text-[#f5f5f5]"
                            >视频</span
                        >
                    </div>
                </template>
            </el-upload>
        </div>
    </div>
</template>

<style scoped lang="scss"></style>