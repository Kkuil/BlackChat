import type { RouteRecordRaw } from "vue-router"

export const routes: RouteRecordRaw[] = [
    {
        path: "/",
        name: "blackchat-home",
        component: () => import("@/views/BlackChatHome/BlackChatHome.vue"),
        children: [
            {
                path: "/",
                redirect: "/chat"
            },
            {
                name: "chat",
                path: "/chat",
                component: () =>
                    import("@/views/BlackChatHome/ChatView/ChatView.vue")
            },
            {
                name: "contact",
                path: "/contact",
                component: () =>
                    import("@/views/BlackChatHome/ContactView/ContactView.vue")
            }
        ]
    },
    {
        path: "/:pathMatch(.*)*",
        name: "NotFound",
        component: () => import("@/views/NotFound.vue")
    }
]
