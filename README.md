# LINE Pay
由spring boot 開發的 LINE Pay 串接程式
先從linpay官網創建 sandbox帳號
從其中取得channelId 與 channelSecret
https://pay.line.me/jp/developers/techsupport/sandbox/testflow?locale=zh_TW
# setting
src\main\resources\application.yml
設定 channelId 與 channelSecret

# 發送請求
Post https://localhost:8082/linepay/request
body
{
    "amount": 1640,
    "orderId": "20240625000000000001",
    "currency": "TWD",
    "packages": [
        {
            "amount": 1640,
            "name": "test123",
            "id": "d1f9be71-7a87-401b-8b8a-560042e7ad45",
            "products": [
                {
                    "quantity": 1,
                    "price": 668,
                    "imageUrl": "",
                    "name": "基礎洗車(3l)",
                    "id": "3642a9ac-3f23-480d-be3c-d78d85e40f3e"
                },
                {
                    "quantity": 1,
                    "price": 468,
                    "imageUrl": "",
                    "name": "基礎洗車(xl)",
                    "id": "8eb8de4b-03ce-42d6-88dc-031c34edfe51"
                },
                {
                    "quantity": 3,
                    "price": 168,
                    "imageUrl": "",
                    "name": "基礎洗車(s)",
                    "id": "d72a5bc0-a2f5-406d-a94a-d8c8801d45df"
                }
            ]
        }
    ],
    "redirectUrls": {
        "cancelUrl": "設置操作取消時返回的網址",
        "confirmUrl": "設置操作完成時返回的網址"
    }
}