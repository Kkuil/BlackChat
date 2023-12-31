export const GroupRoleEnum = {
    MASTER: 1,
    ADMIN: 2,
    MEMBER: 3
}

export const GROUP_ADMIN_ROLE_LIST = [GroupRoleEnum.MASTER, GroupRoleEnum.ADMIN]

export const GROUP_ROLE_MAP: Record<
    string,
    { text: string; bgClr: string; txtClr: string }
> = {
    [GroupRoleEnum.MASTER]: {
        text: "群主",
        bgClr: "#f2c55c",
        txtClr: "#fff"
    },
    [GroupRoleEnum.ADMIN]: {
        text: "管理员",
        bgClr: "#aa6d4b",
        txtClr: "#fff"
    }
}
