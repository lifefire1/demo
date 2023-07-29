const startRecordingButton = document.querySelector('.voice-button');
let mediaRecorder;
let chunks = [];
let isRecording = false; // Флаг для отслеживания состояния записи

startRecordingButton.addEventListener('click', async () => {
    try {
        if (!isRecording) {
            const stream = await navigator.mediaDevices.getUserMedia({ audio: true });
            mediaRecorder = new MediaRecorder(stream);

            mediaRecorder.ondataavailable = event => {
                if (event.data.size > 0) {
                    chunks.push(event.data);
                }
            };

            mediaRecorder.onstop = () => {
                const blob = new Blob(chunks, { type: 'audio/webm' });
                chunks = [];

                // Создаем новый файл с добавленным расширением .wav
                const audioFileName = 'audio_recording.wav';
                const audioFileWithExtension = new File([blob], audioFileName, { type: blob.type });

                // Отправляем записанный аудиофайл на сервер
                uploadAudioFile(audioFileWithExtension);
            };

            mediaRecorder.start();
            isRecording = true;
            startRecordingButton.textContent = 'Остановить запись';
        } else {
            mediaRecorder.stop();
            isRecording = false;
            startRecordingButton.textContent = 'Начать запись';
        }
    } catch (error) {
        console.error('Ошибка при доступе к микрофону: ', error);
    }
});

function uploadAudioFile(file) {
    const formData = new FormData();
    formData.append('audioFile', file);

    // Здесь вы можете указать URL вашего сервера, куда отправлять POST-запрос с аудиофайлом
    const url = '/uploadAudio'; // Например, '/upload-audio'

    fetch(url, {
        method: 'POST',
        body: formData
    })
        .then(response => {
            if (response.ok) {
                // Обработка успешной отправки аудиофайла на сервер
                console.log('Аудиофайл успешно отправлен на сервер');
            } else {
                // Обработка ошибки отправки аудиофайла на сервер
                console.error('Ошибка при отправке аудиофайла на сервер');
            }
        })
        .catch(error => {
            console.error('Ошибка при отправке аудиофайла: ', error);
        });
}