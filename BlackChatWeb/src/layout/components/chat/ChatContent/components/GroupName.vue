<script setup lang="ts">
import { useSessionStore } from "@/stores/session"
import { ref } from "vue"
import { ElMessage, ElMessageBox } from "element-plus"
import { GROUP_ADMIN_ROLE_LIST } from "@/enums/GroupRoleEnum"

const sessionStore = useSessionStore()

// 是否正在更新群名
const isUpdatingGroupName = ref(false)
// 更新群名
const inputGroupName = ref(sessionStore.getSessionInfo.name)

/**
 * 更新群名
 */
const updateGroupName = () => {
    if (inputGroupName.value.length < 3) {
        return ElMessage.error("群名不能少于3个字符")
    }
    if (inputGroupName.value.length > 10) {
        return ElMessage.error("群名不能多于10个字符")
    }
    ElMessageBox.confirm("即将更新群名，是否继续？", "更新群名", {
        confirmButtonText: "确定",
        cancelButtonText: "取消"
    }).then(() => {
        sessionStore.updateGroupInfoHandler({
            groupName: inputGroupName.value
        })
        isUpdatingGroupName.value = false
    })
}
</script>

<template>
    <div class="group-name flex-center text-[18px] cursor-pointer">
        <h1 v-if="!isUpdatingGroupName">
            {{ sessionStore.getSessionInfo.name }}
        </h1>
        <el-input
            v-else
            placeholder="请输入3-10字的群名"
            v-model="inputGroupName"
            minlength="3"
            maxlength="10"
            resize="horizontal"
            clearable
            class="bg-transparent"
            @blur="updateGroupName"
            @keyup.enter="updateGroupName"
        />
        <i
            @click="isUpdatingGroupName = true"
            v-show="
                !isUpdatingGroupName &&
                !sessionStore.isHotFlag &&
                GROUP_ADMIN_ROLE_LIST.includes(
                    sessionStore.currentRoleId as number
                )
            "
            class="iconfont icon-editor text-[20px] ml-[5px] text-[#0094ff]"
        ></i>
        <i
            @click="isUpdatingGroupName = false"
            v-show="isUpdatingGroupName"
            class="iconfont icon-close text-[20px] ml-[5px]"
        ></i>
    </div>
</template>

<style lang="scss">
.group-name {
    .el-input__wrapper {
        background: transparent;

        .el-input__inner {
            font-weight: normal;
            color: #fff;
        }
    }
}
</style>