export const routes = [
    {
        path: "/",
        name: "blackchat-home",
        component: () => import("@/views/BlackChatHome/BlackChatHome.vue")
    },
    {
        path: "/:pathMatch(.*)*",
        name: "NotFound",
        component: () => import("@/views/NotFound.vue")
    }
]
