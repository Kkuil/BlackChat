<script setup lang="ts">
import BlackSortedList from "@/components/BlackSortedList/BlackSortedList.vue"
import { ref, watch } from "vue"
import { useUserStore } from "@/stores/user"
import { useFriendStore } from "@/stores/friend"

const emits = defineEmits<{
    (e: "change", id: string): void
}>()

const userStore = useUserStore()
const friendStore = useFriendStore()

const onChange = (id: string) => {
    emits("change", id)
}

const activeTab = ref<string>("friend")
watch(
    () => userStore.userInfo,
    () => {
        friendStore.getFriends()
    },
    {
        immediate: true
    }
)
</script>

<template>
    <div class="contact-list h-full flex flex-col">
        <div class="flex h-[6%]">
            <ElButton class="add-group flex-1" type="primary">
                创建群聊
            </ElButton>
            <ElButton class="search-friend flex-1" type="primary">
                添加朋友
            </ElButton>
        </div>
        <el-tabs :model-value="activeTab" class="h-[94%]">
            <el-tab-pane label="朋友" name="friend">
                <BlackSortedList
                    title="朋友"
                    class="h-full"
                    :list="friendStore.friends"
                    @change="onChange"
                ></BlackSortedList>
            </el-tab-pane>
            <el-tab-pane label="群聊" name="group">群聊</el-tab-pane>
        </el-tabs>
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