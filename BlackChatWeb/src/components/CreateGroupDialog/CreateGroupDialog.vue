<script setup lang="ts">
import { ref } from "vue"
import { useFriendStore } from "@/stores/friend"
import { createGroup } from "@/api/user"
import { ElMessage, UploadRawFile, UploadUserFile } from "element-plus"
import { convertImageToBase64 } from "@/utils/ImageUtil"
import { MAX_GROUP_AVATAR_SIZE } from "@/constant/group"
import { uploadGroupAvatar } from "@/api/upload"

const friendStore = useFriendStore()

// 创建群聊搜索框
const selectedFriends = ref<number[]>([])
// 创建群聊申请
const applyComment = ref<string>("")
// 群名
const groupName = ref<string>("")
// 是否处于上传头像中
const isUploadingGroupAvatar = ref<boolean>(false)
const groupAvatar = ref<{
    previewGroupAvatarUrl: string
    groupAvatarUrl: string
    groupAvatarFile: UploadRawFile | null
}>({
    // 预览群头像URL
    previewGroupAvatarUrl: "",
    // 群头像url
    groupAvatarUrl: "",
    // 群头像文件
    groupAvatarFile: null
})

/**
 * 创建群聊操作
 */
const createGroupHandler = async () => {
    if (applyComment.value.trim().length <= 0) {
        return ElMessage.error("申请备注不能为空")
    }
    // 上传头像
    if (groupAvatar.value.groupAvatarFile) {
        isUploadingGroupAvatar.value = true
        const result = await uploadGroupAvatar(
            groupAvatar.value.groupAvatarFile as UploadRawFile
        )
        if (result.data) {
            groupAvatar.value.groupAvatarUrl = result.data
        }
        isUploadingGroupAvatar.value = false
    }
    const result = await createGroup({
        uidList: selectedFriends.value,
        msg: applyComment.value,
        groupName: groupName.value,
        groupAvatar: groupAvatar.value.groupAvatarUrl
    })
    if (result.data) {
        ElMessage.success(result.message)
        reset()
    }
}

const reset = () => {
    applyComment.value = ""
    previewGroupAvatar.value = ""
    groupAvatar.value.previewGroupAvatarUrl = ""
    selectedFriends.value = []
}

/**
 * 群头像预览
 * @param file
 */
const previewGroupAvatar = async (file: UploadUserFile) => {
    if (file.size > MAX_GROUP_AVATAR_SIZE) {
        return ElMessage.error("群头像大小不能超过500KB")
    }
    groupAvatar.value.groupAvatarFile = file.raw as UploadRawFile
    const base64 = (await convertImageToBase64(file.raw)) as string
    groupAvatar.value.previewGroupAvatarUrl = base64
}

/**
 * 移除群头像
 */
const removeGroupAvatar = () => {
    groupAvatar.value.previewGroupAvatarUrl = ""
}
</script>

<template>
    <el-transfer
        :props="{
            key: 'uid',
            label: 'name'
        }"
        v-model="selectedFriends"
        :data="friendStore.friends"
        :titles="['未选中', '已选中']"
    />
    <div class="flex-center mt-[5px]">
        <el-popover
            title="申请备注"
            placement="right"
            :width="200"
            trigger="click"
        >
            <template #reference>
                <el-button type="primary">创建</el-button>
            </template>
            <template #default>
                <el-input
                    v-model="applyComment"
                    placeholder="请输入申请备注"
                    :max="200"
                    size="small"
                    class="text-[#000]"
                />
                <h2 class="text-[13px] my-[3px]">群名</h2>
                <el-input
                    v-model="groupName"
                    placeholder="请输入群名"
                    :max="200"
                    size="small"
                    class="text-[#000]"
                />
                <el-upload
                    name="groupAvatar"
                    class="upload-group-avatar mt-[5px]"
                    :on-remove="removeGroupAvatar"
                    :auto-upload="false"
                    @change="previewGroupAvatar"
                    :limit="1"
                    accept="image"
                    v-loading="isUploadingGroupAvatar"
                >
                    <el-button type="primary" size="small">
                        点击上传群头像
                    </el-button>
                    <template #tip>
                        <div class="el-upload__tip">图片大小不超过500KB</div>
                    </template>
                </el-upload>
                <el-avatar
                    v-if="groupAvatar.previewGroupAvatarUrl"
                    size="default"
                    :src="groupAvatar.previewGroupAvatarUrl"
                />
                <div class="operation mt-[5px]">
                    <el-button
                        type="primary"
                        size="small"
                        @click="createGroupHandler"
                    >
                        确定
                    </el-button>
                </div>
            </template>
        </el-popover>
    </div>
</template>

<style lang="scss">
.el-transfer-panel {
    background-color: #13171e;

    .el-transfer-panel__header {
        background-color: #13171e;
    }
}
</style>