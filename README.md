### Концепция:
Добавляем канал с youtube по имени, указакнному в url, после чего получаем доступ к ряду опций:
* Обновлять видео канала за N дней
* Помечать видео просмотренным
* Получать превью видео (для телеграмма)
* Получать все не просмотренные видео
### Способы взаимодействия:
1. Браузер (class **Controller**)
2. Телеграм бот (class **Bot**)
### Примеры:
### :iphone:Telegram
![изображение](https://user-images.githubusercontent.com/87547390/205506351-4f2ff66a-725e-4b3a-bd0c-23cb15a22b82.png)
![изображение](https://user-images.githubusercontent.com/87547390/205506386-eb7450c3-93f1-472a-ac9f-73a74a8b79c4.png)

### :computer:Browser

__GET__ ``` /channel/all```
```
  {
    "id": 1,
    "urlName": "gohamedia",
    "title": "GohaMedia",
    "videos": [
      {
        "id": 2,
        "title": "THRONE AND LIBERTY — МНОГО НОВОЙ ИНФОРМАЦИИ О САМОЙ ОЖИДАЕМОЙ MMORPG",
        "videoUrl": "https://www.youtube.com//watch?v=WKyRwy58-rc",
        "previewUrl": "https://i.ytimg.com/vi/WKyRwy58-rc/hqdefault.jpg?sqp=-oaymwEbCKgBEF5IVfKriqkDDggBFQAAiEIYAXABwAEG",
        "uploadDate": "2 дек. 2022",
        "watched": false
      },
      {
        "id": 1,
        "title": "НОВОСТИ MMORPG: НОВАЯ MMORPG ОТ KRAFTON, ОСАДЫ В ODIN VALHALLA RISING, КАРТА МИРА MMORPG PIONER",
        "videoUrl": "https://www.youtube.com//watch?v=02MYtIoFsDQ",
        "previewUrl": "https://i.ytimg.com/vi/02MYtIoFsDQ/hqdefault.jpg?sqp=-oaymwEbCKgBEF5IVfKriqkDDggBFQAAiEIYAXABwAEG",
        "uploadDate": "4 дек. 2022",
        "watched": false
      }
    ]
  }
```

__GET__ ``` /channel/1/video/2?isWatched=true```

```Parameter "isWatched" in the video by id 2 was set to true```

__GET__ ``` /channel/1/videos```

```
  {
    "id": 1,
    "title": "НОВОСТИ MMORPG: НОВАЯ MMORPG ОТ KRAFTON, ОСАДЫ В ODIN VALHALLA RISING, КАРТА МИРА MMORPG PIONER",
    "videoUrl": "https://www.youtube.com//watch?v=02MYtIoFsDQ",
    "previewUrl": "https://i.ytimg.com/vi/02MYtIoFsDQ/hqdefault.jpg?sqp=-oaymwEbCKgBEF5IVfKriqkDDggBFQAAiEIYAXABwAEG",
    "uploadDate": "4 дек. 2022",
    "watched": false
  }
```
  
