

# Dockerfile cho Vue.js
# Sử dụng hình ảnh Node.js 16 để xây dựng ứng dụng
FROM node:16 AS build

# Đặt thư mục làm việc
WORKDIR /app

# Sao chép tệp package.json và package-lock.json
COPY package*.json ./

# Cài đặt các gói cần thiết
RUN npm install

# Sao chép toàn bộ mã nguồn vào hình ảnh
COPY . .

# Xây dựng ứng dụng
RUN npm run build

# Sử dụng Nginx để phục vụ ứng dụng
FROM nginx:alpine

# Sao chép tệp xây dựng từ bước trước vào Nginx
COPY --from=build /app/.output/public /usr/share/nginx/html

# Mở port 80 để phục vụ ứng dụng
EXPOSE 80

# Khởi động Nginx
CMD ["nginx", "-g", "daemon off;"]