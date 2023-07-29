function sendJSON() {
    const name = document.getElementById('name').value;
    const email = document.getElementById('email').value;
    const password = document.getElementById('password').value;
    const confirmPassword = document.getElementById('confirmPassword').value;
    const phoneNumber = document.getElementById('phoneNumber').value;

    const data = {
        name: name,
        email: email,
        password: password,
        confirmPassword: confirmPassword,
        phoneNumber: phoneNumber
    };

    // Преобразование объекта в JSON-строку
    const jsonData = JSON.stringify(data);

    // Здесь вы можете добавить код для отправки данных на сервер
    // Например, с использованием Fetch API или XMLHttpRequest
    // Но для примера, просто выведем JSON-строку в консоль
    console.log(jsonData);
}
