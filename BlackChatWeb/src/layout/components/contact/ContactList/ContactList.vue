<script setup lang="ts">
import BlackSortedList from "@/components/BlackSortedList/BlackSortedList.vue"
import { ref } from "vue"

const emits = defineEmits<{
    (e: "change", id: string): void
}>()

const list = [
    {
        name: "Kkuil",
        avatar: "https://thirdwx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTJkQXoMolPPP0JVa8DF1kJ50nicQ1HJvYwXBoicBNVwlzlFNB23m0KCmd4AML7jE7icpwU7xCZJZ5pMA/132"
    },
    {
        name: "akuil",
        avatar: "https://thirdwx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTJkQXoMolPPP0JVa8DF1kJ50nicQ1HJvYwXBoicBNVwlzlFNB23m0KCmd4AML7jE7icpwU7xCZJZ5pMA/132"
    },
    {
        name: "bkuil",
        avatar: "https://thirdwx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTJkQXoMolPPP0JVa8DF1kJ50nicQ1HJvYwXBoicBNVwlzlFNB23m0KCmd4AML7jE7icpwU7xCZJZ5pMA/132"
    },
    {
        name: "bauil",
        avatar: "https://thirdwx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTJkQXoMolPPP0JVa8DF1kJ50nicQ1HJvYwXBoicBNVwlzlFNB23m0KCmd4AML7jE7icpwU7xCZJZ5pMA/132"
    },
    {
        name: "ckuil",
        avatar: "https://thirdwx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTJkQXoMolPPP0JVa8DF1kJ50nicQ1HJvYwXBoicBNVwlzlFNB23m0KCmd4AML7jE7icpwU7xCZJZ5pMA/132"
    },
    {
        name: "dkuil",
        avatar: "https://thirdwx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTJkQXoMolPPP0JVa8DF1kJ50nicQ1HJvYwXBoicBNVwlzlFNB23m0KCmd4AML7jE7icpwU7xCZJZ5pMA/132"
    },
    {
        name: "ekuil",
        avatar: "https://thirdwx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTJkQXoMolPPP0JVa8DF1kJ50nicQ1HJvYwXBoicBNVwlzlFNB23m0KCmd4AML7jE7icpwU7xCZJZ5pMA/132"
    },
    {
        name: "fkuil",
        avatar: "https://thirdwx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTJkQXoMolPPP0JVa8DF1kJ50nicQ1HJvYwXBoicBNVwlzlFNB23m0KCmd4AML7jE7icpwU7xCZJZ5pMA/132"
    },
    {
        name: "ekuil",
        avatar: "https://thirdwx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTJkQXoMolPPP0JVa8DF1kJ50nicQ1HJvYwXBoicBNVwlzlFNB23m0KCmd4AML7jE7icpwU7xCZJZ5pMA/132"
    }
]

const onChange = (id: string) => {
    emits("change", id)
}

const activeTab = ref<string>("contact")
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
            <el-tab-pane label="联系人" name="contact">
                <BlackSortedList
                    title="联系人"
                    class="h-full"
                    :list="list"
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