const timeInMinutes = 2;
let time = timeInMinutes * 60
let timerInMilliseconds = time * 1000;

const countDown = document.getElementById('countdown');

function updateCountDown() {
    const minutes = Math.floor(time / 60);
    let seconds = time % 60;

    seconds = seconds < 10 ? '0' + seconds : seconds;

    countDown.innerText = `${minutes}:${seconds}`;
    time--;
}

setInterval(updateCountDown, 1000);

setTimeout(function(){
    document.getElementById("form-no").click();
}, timerInMilliseconds);