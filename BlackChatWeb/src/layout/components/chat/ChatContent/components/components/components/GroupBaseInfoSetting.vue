<script setup lang="ts">
import { ref } from "vue"
import { useSessionStore } from "@/stores/session"
import { Plus } from "@element-plus/icons-vue"
import type { UploadUserFile } from "element-plus"
import { convertImageToBase64 } from "@/utils/ImageUtil"
import { updateGroupInfo } from "@/api/group"

const sessionStore = useSessionStore()

const groupInfo = ref<{
    groupName: string
    groupAvatar: string
}>({
    groupName: "",
    groupAvatar: sessionStore.getSessionInfo.avatar
})

const previewGroupUpload = async (file: UploadUserFile) => {
    console.log(file)
    groupInfo.value.groupAvatar = (await convertImageToBase64(
        file.raw
    )) as string
}

const resetGroupInfo = () => {
    groupInfo.value = {
        groupName: "",
        groupAvatar: sessionStore.getSessionInfo.avatar
    }
}

const updateGroupInfoHandler = async () => {
    const result = await updateGroupInfo({
        groupId: sessionStore.getSessionInfo.roomId,
        ...groupInfo.value
    })
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
        <el-form-item label="群头像">
            <el-upload
                class="group-avatar"
                :auto-upload="false"
                :show-file-list="false"
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
            @submit="updateGroupInfoHandler"
        >
            提交
        </el-button>
    </el-form>
</template>

<style scoped lang="scss"></style>