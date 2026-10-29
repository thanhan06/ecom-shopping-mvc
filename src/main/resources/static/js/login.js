document.addEventListener('DOMContentLoaded', function() {
    const loginForm = document.getElementById('loginForm');
    const usernameInput = document.getElementById('username');
    const passwordInput = document.getElementById('password');
    const usernameError = document.getElementById('usernameError');
    const passwordError = document.getElementById('passwordError');
    const globalError = document.getElementById('globalError'); // Lấy thẻ báo lỗi chung

    // Hàm tiện ích để xóa lỗi chung nếu nó đang tồn tại
    function clearGlobalError() {
        if (globalError) {
            globalError.textContent = '';
        }
    }

    // Bắt sự kiện gõ phím trên ô Username để xóa lỗi
    if (usernameInput) {
        usernameInput.addEventListener('input', function() {
            usernameError.textContent = ''; 
            clearGlobalError();
        });
    }

    // Bắt sự kiện gõ phím trên ô Password để xóa lỗi
    if (passwordInput) {
        passwordInput.addEventListener('input', function() {
            passwordError.textContent = ''; 
            clearGlobalError();
        });
    }

    // Xử lý validate khi submit form
    if (loginForm) {
        loginForm.addEventListener('submit', function(event) {
            let isValid = true;
            
            usernameError.textContent = '';
            passwordError.textContent = '';
            clearGlobalError();
            
            const msgUserBlank = window.I18N_LOGIN.userBlank;
            const msgUserSize = window.I18N_LOGIN.userSize;
            const msgPassBlank = window.I18N_LOGIN.passBlank;
            const msgPassSize = window.I18N_LOGIN.passSize;

            if (usernameInput.value.trim() === '') {
                usernameError.textContent = msgUserBlank;
                isValid = false;
            } else if (usernameInput.value.length > 20) {
                usernameError.textContent = msgUserSize;
                isValid = false;
            }

            if (isValid) {
                if (passwordInput.value.trim() === '') {
                    passwordError.textContent = msgPassBlank;
                    isValid = false;
                } else if (passwordInput.value.length < 5) {
                    passwordError.textContent = msgPassSize;
                    isValid = false;
                }
            }

            if (!isValid) {
                event.preventDefault();
            }
        });
    }
});