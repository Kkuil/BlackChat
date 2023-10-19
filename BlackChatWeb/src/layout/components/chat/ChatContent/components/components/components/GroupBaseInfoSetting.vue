<script setup lang="ts">
import { ref } from "vue"
import { useSessionStore } from "@/stores/session"
import { Plus } from "@element-plus/icons-vue"
import type { UploadRawFile, UploadUserFile } from "element-plus"
import { ElMessage } from "element-plus"
import { convertImageToBase64 } from "@/utils/ImageUtil"
import { MAX_GROUP_AVATAR_SIZE } from "@/constant/global"
import { uploadGroupAvatar } from "@/api/upload"

const sessionStore = useSessionStore()

const groupInfo = ref<{
    groupName: string
    groupAvatar: string
}>({
    groupName: "",
    groupAvatar: sessionStore.getSessionInfo.avatar
})

const groupAvatarFile = ref<UploadUserFile[]>([])

const isUploadingGroupAvatar = ref<boolean>(false)

const previewGroupUpload = async (file: UploadUserFile) => {
    // 判断图片大小
    if (file.size > MAX_GROUP_AVATAR_SIZE) {
        return ElMessage.error(
            "图片大小不能超过" + MAX_GROUP_AVATAR_SIZE / 1024 + "KB"
        )
    }
    groupInfo.value.groupAvatar = (await convertImageToBase64(
        file.raw
    )) as string
}

const updateGroupInfoHandler = async () => {
    // 判断图片大小
    if (groupAvatarFile.value.length) {
        isUploadingGroupAvatar.value = true
        if (groupAvatarFile.value[0].size > MAX_GROUP_AVATAR_SIZE) {
            return ElMessage.error(
                "图片大小不能超过" + MAX_GROUP_AVATAR_SIZE / 1024 + "KB"
            )
        }
        // 上传头像
        const result = await uploadGroupAvatar(
            groupAvatarFile.value[0].raw as UploadRawFile
        )
        isUploadingGroupAvatar.value = false
        if (result.data) {
            groupInfo.value.groupAvatar = result.data
        } else {
            return ElMessage.error("上传头像失败，请稍后重试")
        }
    }
    const isSuccess = sessionStore.updateGroupInfoHandler(groupInfo.value)
    if (isSuccess) {
        ElMessage.success("修改成功")
        resetGroupInfo()
    }
}

const resetGroupInfo = () => {
    groupInfo.value = {
        groupName: "",
        groupAvatar: sessionStore.getSessionInfo.avatar
    }
}
</script>

<template>
    <el-form
        label-position="top"
        label-width="100px"
        :model="groupInfo"
        style="max-width: 460px"
    >
        <el-form-item label="群名">
            <el-input
                v-model="groupInfo.groupName"
                :placeholder="sessionStore.getSessionInfo.name"
            />
        </el-form-item>
        <el-form-item label="群头像" v-loading="isUploadingGroupAvatar">
            <el-upload
                v-model:file-list="groupAvatarFile"
                class="group-avatar"
                :auto-upload="false"
                :show-file-list="false"
                accept="image"
                :limit="1"
                @change="previewGroupUpload"
            >
                <img
                    v-if="groupInfo.groupAvatar"
                    :src="groupInfo.groupAvatar"
                    class="avatar h-[150px]"
                    :alt="sessionStore.getSessionInfo.name"
                />
                <el-icon v-else class="avatar-uploader-icon">
                    <Plus />
                </el-icon>
            </el-upload>
        </el-form-item>
        <el-button size="small" @click="resetGroupInfo">重置</el-button>
        <el-button
            size="small"
            type="primary"
            native-type="submit"
            @click.prevent="updateGroupInfoHandler"
        >
            提交
        </el-button>
    </el-form>
</template>

<style scoped lang="scss"></style>