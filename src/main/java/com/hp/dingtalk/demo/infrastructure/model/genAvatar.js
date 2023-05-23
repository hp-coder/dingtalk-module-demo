// The DingTalk-style avatar generating method
function nameBasedAvatarGenerator(username) {
    const canvas = document.createElement('canvas')
    canvas.width = 50
    canvas.height = 50
    canvas.style.borderRadius = '5px'
    //配置
    const ctx = canvas.getContext('2d')
    ctx.fillStyle = '#3d8bf3'
    ctx.fillRect(0, 0, canvas.width, canvas.height)
    ctx.fillStyle = '#FFFFFF'
    //取后两位
    if (username.length >= 3) {
        username = username.substr(-2, 2)
    }
    ctx.font = '20px Arial'
    ctx.textAlign = 'center'
    ctx.textBaseline = 'middle'
    // canvas.height/2.0 中文稍微有点偏上 , /1.9 看着正中间
    ctx.fillText(username, canvas.width / 2, canvas.height / 1.9)
    return canvas.toDataURL()
}
