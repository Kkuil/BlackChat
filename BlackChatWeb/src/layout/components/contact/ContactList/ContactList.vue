<script setup lang="ts">
import BlackSortedList from "@/components/BlackSortedList/BlackSortedList.vue"
import { onBeforeUnmount, onMounted, ref, watch } from "vue"
import { useUserStore } from "@/stores/user"
import { useFriendStore } from "@/stores/friend"
import AddFriendDialog from "@/components/AddFriendDialog/AddFriendDialog.vue"
import CreateGroupDialog from "@/components/CreateGroupDialog/CreateGroupDialog.vue"

const emits = defineEmits<{
    (e: "change", id: string): void
}>()

const userStore = useUserStore()
const friendStore = useFriendStore()

// tab
const activeTab = ref<string>("friend")
// 展示添加朋友弹框
const showAddFriend = ref<boolean>(false)
// 展示创建群聊弹框
const showCreateGroup = ref<boolean>(false)

/**
 * 添加朋友
 */
const addFriend = () => {
    showAddFriend.value = true
}

/**
 * 创建群聊
 */
const createGroup = () => {
    showCreateGroup.value = true
}

const onChange = (id: string) => {
    emits("change", id)
}

watch(
    () => userStore.userInfo,
    () => {
        friendStore.getFriends()
    },
    {
        immediate: true
    }
)

/**
 * ctrl + k事件
 * @param event
 */
const ctrlKEvent = function (event: KeyboardEvent) {
    if (event.ctrlKey && event.key === "k") {
        event.preventDefault()
        showAddFriend.value = !showAddFriend.value
    }
}

onMounted(() => {
    // 监听事件
    document.addEventListener("keydown", ctrlKEvent)
})

onBeforeUnmount(() => {
    // 移除事件
    document.removeEventListener("keydown", ctrlKEvent)
})
</script>

<template>
    <div class="contact-list h-full flex flex-col">
        <div class="flex h-[6%]">
            <ElButton
                class="add-group flex-1"
                type="primary"
                @click="createGroup"
            >
                创建群聊
            </ElButton>
            <ElButton
                class="search-friend flex-1"
                type="primary"
                @click="addFriend"
            >
                添加朋友(Ctrl+K)
            </ElButton>
        </div>
        <el-tabs :model-value="activeTab" class="h-[94%]">
            <el-tab-pane label="朋友" name="friend">
                <BlackSortedList
                    class="h-full"
                    :list="friendStore.friends"
                    @change="onChange"
                ></BlackSortedList>
            </el-tab-pane>
        </el-tabs>
        <el-dialog v-model="showAddFriend" :show-close="false" class="bg-third">
            <AddFriendDialog />
        </el-dialog>
        <el-dialog
            v-model="showCreateGroup"
            :show-close="false"
            class="bg-third flex-center"
        >
            <CreateGroupDialog />
        </el-dialog>
    </div>
</template>

<style lang="scss">
.contact-list {
    --el-text-color-primary: #fff;
    --el-border-color-light: #777;

    .el-tabs__content {
        height: 91% !important;

        .el-tab-pane {
            height: 100% !important;
        }
    }
}
</style>