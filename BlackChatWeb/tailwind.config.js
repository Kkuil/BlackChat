/** @type {import("tailwindcss").Config} */
export default {
    content: ["./index.html", "./src/**/*.{vue,js,ts,jsx,tsx}"],
    theme: {
        extend: {},
        colors: {
            primary: "#272e3d",
            secondary: "#303442",
            third: "#424656"
        }
    },
    important: true,
    plugins: []
}
