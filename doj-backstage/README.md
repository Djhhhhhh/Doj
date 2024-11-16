# doj-backstage（doj项目后台）

## 基础命令

### 项目所含包引入
```
pnpm install
```

### 项目启动
```
pnpm dev
```

### 项目打包
```
pnpm build
```

### 自动生成axios api接口 
```
openapi --input http://localhost:8111/api/judge/v2/api-docs --output ./api --client axios
openapi --input http://localhost:8111/api/user/v2/api-docs --output ./api --client axios
openapi --input http://localhost:8111/api/question/v2/api-docs --output ./api --client axios
```