<script setup lang="ts">
import { useSessionStore } from "@/stores/session"
import { popUpLoginDialog } from "@/utils/popDialog/popLoginDialog"
import { useUserStore } from "@/stores/user"
import { Avatar } from "@element-plus/icons-vue"
import { onMounted, ref } from "vue"
import AddGroupDialog from "@/components/InviteGroupDialog/InviteGroupDialog.vue"
import { GROUP_ROLE_MAP } from "@/enums/GroupRoleEnum"

const sessionStore = useSessionStore()

const userStore = useUserStore()
// 展示创建群聊弹框
const showAddGroup = ref<boolean>(false)

/**
 * 创建群聊
 */
const inviteFriendToAddGroup = () => {
    showAddGroup.value = true
}

onMounted(() => {
    sessionStore.switchSession(sessionStore.sessionInfo.chattingId)
})
</script>

<template>
    <div
        class="online-list flex flex-col ml-[10px] bg-secondary rounded-[10px] text-[#fff] p-[10px] relative"
    >
        <div
            v-if="!userStore.userInfo.uid && !sessionStore.isHotFlag"
            :class="!userStore.userInfo.name ? 'backdrop-blur-md' : ''"
            class="w-full h-full absolute top-0 left-0 flex-center text-[12px] font-serif rounded-[10px]"
        >
            点击
            <a
                class="text-[#0094ff] text-sm cursor-pointer"
                @click="popUpLoginDialog"
            >
                登录
            </a>
            后再进行查看吧
        </div>
        <h1
            class="online-count flex justify-between items-center h-[30px] text-[14px]"
        >
            <span>总人数：{{ sessionStore.sessionInfo.totalCount }}</span>
            <i
                v-if="!sessionStore.isHotFlag"
                class="iconfont icon-plus w-[20px] h-[20px] text-[12px] bg-[#0094ff] rounded-full flex-center cursor-pointer"
                title="点击邀请好友"
                @click="inviteFriendToAddGroup"
            ></i>
        </h1>
        <ul class="list flex-1 overflow-y-auto overflow-x-hidden">
            <TransitionGroup tag="ul" name="fade" class="user-list">
                <li
                    class="item flex items-center py-[7px] cursor-pointer mb-[5px] hover:bg-[#263242] rounded-[5px] px-[7px]"
                    v-for="member in sessionStore.sessionInfo.memberList"
                    :key="member.uid"
                >
                    <el-badge
                        is-dot
                        :type="
                            member.activeStatus === 1 ? 'primary' : 'warning'
                        "
                    >
                        <el-avatar :size="'small'" :src="member.avatar">
                            <el-icon :size="18">
                                <Avatar />
                            </el-icon>
                        </el-avatar>
                    </el-badge>
                    <div
                        class="line-clamp-1 overflow-hidden overflow-ellipsis text-[14px] ml-[5px]"
                    >
                        {{ member.name }}
                    </div>
                    <div
                        v-if="GROUP_ROLE_MAP[member.roleId]"
                        class="ml-[10px] text-[10px] border-[1px] border-[#fff] w-auto h-full rounded-[5px] flex-center px-[5px]"
                        :class="`bg-[${
                            GROUP_ROLE_MAP[member.roleId].bgClr
                        }] text-[${GROUP_ROLE_MAP[member.roleId].txtClr}]`"
                    >
                        {{ GROUP_ROLE_MAP[member.roleId].text }}
                    </div>
                </li>
            </TransitionGroup>
            <li
                class="w-full flex-center text-[#fff]"
                v-if="!sessionStore.listMemberPage.isLast"
                v-observe="sessionStore.getMemberList"
            >
                <i class="iconfont icon-loading animate-spin"></i>
                <span class="text-[12px] ml-[5px]">成员列表加载中</span>
            </li>
        </ul>
        <el-dialog
            v-model="showAddGroup"
            :show-close="false"
            class="bg-third flex-center"
        >
            <AddGroupDialog />
        </el-dialog>
    </div>
</template>

<style scoped>
.online-list .list {
    --el-color-primary: #adff2f;
    --el-color-warning: #ccc;
}

.online-list .el-badge__content {
    border: 0;
}

/* 1. 声明过渡效果 */
.fade-move,
.fade-enter-active,
.fade-leave-active {
    transition: all 0.5s cubic-bezier(0.55, 0, 0.1, 1);
}

/* 2. 声明进入和离开的状态 */
.fade-enter-from,
.fade-leave-to {
    opacity: 0;
    transform: scaleY(0.01) translate(30px, 0);
}

/* 3. 确保离开的项目被移除出了布局流
      以便正确地计算移动时的动画效果。 */
.fade-leave-active {
    position: absolute;
}
</style>