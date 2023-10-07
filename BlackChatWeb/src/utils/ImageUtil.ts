/**
 * 将图片对象转为base64进行预览
 * @param file
 */
export function convertImageToBase64(file) {
    return new Promise((resolve, reject) => {
        const reader = new FileReader()
        reader.readAsDataURL(file)
        reader.onload = () => resolve(reader.result)
        reader.onerror = (error) => reject(error)
    })
}

/**
 * 异步获取图片大小
 * @param file
 */
export function getImageSize(file: File, src: string) {
    return new Promise((resolve, reject) => {
        const reader = new FileReader()
        reader.onload = function (event) {
            const image = new Image()
            image.onload = function () {
                resolve({
                    width: image.width,
                    height: image.height
                })
                image.onerror = function (error) {
                    reject(error)
                }
                image.src = event.target.result
            }
            reader.onerror = function (error) {
                console.log(error)
                reject(error)
            }
            image.src = src
        }
        reader.readAsDataURL(file)
    })
}
