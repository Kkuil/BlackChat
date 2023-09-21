# 配置Nginx服务器
FROM nginx:1.21-alpine

# 将Vue静态资源复制到Nginx的默认站点目录
COPY --from=0 /app/dist /usr/share/nginx/html

# 暴露Nginx服务器的端口号
EXPOSE 80

# 启动容器时运行Nginx服务器
CMD ["nginx", "-g", "daemon off;"]