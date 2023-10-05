<script setup lang="ts">
import SvgIcon from "@/components/SvgIcon/SvgIcon.vue"
import { useSessionStore } from "@/stores/session"
import { popUpLoginDialog } from "@/utils/popDialog/popLoginDialog"
import { useUserStore } from "@/stores/user"

const sessionStore = useSessionStore()

const userStore = useUserStore()
</script>

<template>
    <div
        class="online-list flex flex-col ml-[10px] bg-secondary rounded-[10px] text-[#fff] p-[10px] relative"
    >
        <div
            v-if="!userStore.userInfo.name"
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
        <h1 class="online-count flex items-center h-[30px] text-[14px]">
            总人数：{{ sessionStore.getSessionInfo.totalCount }}
        </h1>
        <ul
            class="list flex-1 overflow-y-auto overflow-x-hidden"
            v-infinite-scroll="sessionStore.getMemberList"
            v-if="sessionStore.getSessionInfo.totalCount"
        >
            <TransitionGroup
                v-show="sessionStore.sessionInfo.memberList.length"
                tag="ul"
                name="fade"
                class="user-list"
            >
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
                        <el-avatar :size="'small'" :src="member.avatar" />
                    </el-badge>
                    <div
                        class="w-full line-clamp-1 overflow-hidden overflow-ellipsis text-[14px] ml-[5px]"
                    >
                        {{ member.name }}
                    </div>
                </li>
            </TransitionGroup>
            <li
                v-if="sessionStore.listPage.isLast"
                class="w-full flex-center text-[#5b6061] text-[12px]"
            >
                暂无更多成员
            </li>
            <li
                class="w-full flex-center"
                v-observe="sessionStore.getMemberList"
                v-if="!sessionStore.listPage.isLast"
            >
                <i class="iconfont icon-loading animate-spin"></i>
            </li>
        </ul>
        <el-empty class="w-full flex-1 flex-center" v-else>
            <template #image>
                <div class="w-full h-full flex-center">
                    <svg-icon icon-class="group" class="text-[50px]" />
                </div>
            </template>
            <template #description>
                <div class="font-serif text-[13px]">
                    本房间还没有成员，去邀请一个吧~
                </div>
                <ElButton type="primary" class="mt-[10px]" size="small">
                    邀请朋友
                </ElButton>
            </template>
        </el-empty>
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