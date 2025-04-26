const translations = {
    en: {
        signinTab: "Sign In",
        signupTab: "Sign Up",
        emailLabel: "Email",
        usernameLabel: "Username",
        passwordLabel: "Password",
        confirmPasswordLabel: "Confirm Password",
        rememberMe: "Remember Me",
        forgotPassword: "Forgot Password?",
        signinButton: "Sign In",
        signupButton: "Sign Up",
        emailError: "Please enter a valid email address.",
        usernameError: "Username must contain only letters, numbers, dashes, or underscores.",
        passwordError: "Password must be at least 8 characters long.",
        confirmPasswordError: "Passwords do not match.",
        usernameTaken: "Username is already taken."
    },
    ru: {
        signinTab: "Войти",
        signupTab: "Зарегистрироваться",
        emailLabel: "Электронная почта",
        usernameLabel: "Имя пользователя",
        passwordLabel: "Пароль",
        confirmPasswordLabel: "Подтвердить пароль",
        rememberMe: "Запомнить меня",
        forgotPassword: "Забыли пароль?",
        signinButton: "Войти",
        signupButton: "Зарегистрироваться",
        emailError: "Пожалуйста, введите действительный адрес электронной почты.",
        usernameError: "Имя пользователя должно содержать только буквы, цифры, дефис или подчеркивание.",
        passwordError: "Пароль должен содержать не менее 8 символов.",
        confirmPasswordError: "Пароли не совпадают.",
        usernameTaken: "Это имя пользователя уже занято."
    },
    kz: {
        signinTab: "Кіру",
        signupTab: "Тіркелу",
        emailLabel: "Электрондық пошта",
        usernameLabel: "Пайдаланушы аты",
        passwordLabel: "Құпиясөз",
        confirmPasswordLabel: "Құпиясөзді растау",
        rememberMe: "Мені есте сақтау",
        forgotPassword: "Құпиясөзді ұмыттыңыз ба?",
        signinButton: "Кіру",
        signupButton: "Тіркелу",
        emailError: "Жарамды электрондық пошта мекенжайын енгізіңіз.",
        usernameError: "Пайдаланушы аты әріптерден, сандардан және - немесе _ таңбаларынан тұруы керек.",
        passwordError: "Құпиясөз кемінде 8 таңбадан тұруы керек.",
        confirmPasswordError: "Құпиясөздер сәйкес келмейді.",
        usernameTaken: "Бұл пайдаланушы аты бос емес."
    }
};

let currentLang = 'en';

// Tab Switching
document.querySelectorAll('.tab').forEach(tab => {
    tab.addEventListener('click', () => {
        document.querySelectorAll('.tab').forEach(t => t.classList.remove('active'));
        document.querySelectorAll('.auth-form').forEach(f => f.classList.remove('active'));
        tab.classList.add('active');
        document.getElementById(`${tab.dataset.tab}-form`).classList.add('active');
    });
});

// Validation
const usernameRegex = /^[a-zA-Z0-9_-]+$/;
const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
const passwordMinLength = 8;

function validateEmail(email, errorElement) {
    if (!emailRegex.test(email)) {
        errorElement.style.display = 'block';
        return false;
    }
    errorElement.style.display = 'none';
    return true;
}

function validateUsername(username, errorElement) {
    if (!usernameRegex.test(username)) {
        errorElement.style.display = 'block';
        return false;
    }
    errorElement.style.display = 'none';
    return true;
}

function validatePassword(password, errorElement) {
    if (password.length < passwordMinLength) {
        errorElement.style.display = 'block';
        return false;
    }
    errorElement.style.display = 'none';
    return true;
}

function validateConfirmPassword(password, confirmPassword, errorElement) {
    if (password !== confirmPassword) {
        errorElement.style.display = 'block';
        return false;
    }
    errorElement.style.display = 'none';
    return true;
}

// Sign In Form Submission
document.getElementById('signin-form').addEventListener('submit', async (e) => {
    e.preventDefault();
    const username = document.getElementById('signin-username').value;
    const password = document.getElementById('signin-password').value;
    const usernameError = document.getElementById('signin-username-error');
    const passwordError = document.getElementById('signin-password-error');

    const isUsernameValid = validateUsername(username, usernameError);
    const isPasswordValid = validatePassword(password, passwordError);

    if (isUsernameValid && isPasswordValid) {
        const data = { username, password };
        try {
            const response = await fetch('/api/signin', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(data)
            });
            if (response.ok) {
                window.location.href = '/dashboard.html';
            } else {
                alert('Sign in failed. Please check your credentials.');
            }
        } catch (error) {
            console.error('Error:', error);
            alert('An error occurred. Please try again.');
        }
    }
});

// Sign Up Form Submission
document.getElementById('signup-form').addEventListener('submit', async (e) => {
    e.preventDefault();
    const email = document.getElementById('signup-email').value;
    const username = document.getElementById('signup-username').value;
    const password = document.getElementById('signup-password').value;
    const confirmPassword = document.getElementById('signup-confirm-password').value;
    const emailError = document.getElementById('signup-email-error');
    const usernameError = document.getElementById('signup-username-error');
    const usernameTakenError = document.getElementById('username-taken-error');
    const passwordError = document.getElementById('signup-password-error');
    const confirmPasswordError = document.getElementById('confirm-password-error');

    const isEmailValid = validateEmail(email, emailError);
    const isUsernameValid = validateUsername(username, usernameError);
    const isPasswordValid = validatePassword(password, passwordError);
    const isConfirmPasswordValid = validateConfirmPassword(password, confirmPassword, confirmPasswordError);

    if (isEmailValid && isUsernameValid && isPasswordValid && isConfirmPasswordValid) {
        const data = { email, username, password };
        try {
            const response = await fetch('/api/signup', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(data)
            });
            if (response.ok) {
                window.location.href = '/verify.html';
            } else if (response.status === 409) {
                usernameTakenError.style.display = 'block';
            } else {
                alert('Sign up failed. Please try again.');
            }
        } catch (error) {
            console.error('Error:', error);
            alert('An error occurred. Please try again.');
        }
    }
});

// Language Switching
function updateLanguage(lang) {
    currentLang = lang;
    document.querySelector('.tab[data-tab="signin"]').textContent = translations[lang].signinTab;
    document.querySelector('.tab[data-tab="signup"]').textContent = translations[lang].signupTab;
    document.querySelector('#signin-form label[for="signin-username"]').textContent = translations[lang].usernameLabel;
    document.querySelector('#signin-form label[for="signin-password"]').textContent = translations[lang].passwordLabel;
    document.querySelector('#signup-form label[for="signup-email"]').textContent = translations[lang].emailLabel;
    document.querySelector('#signup-form label[for="signup-username"]').textContent = translations[lang].usernameLabel;
    document.querySelector('#signup-form label[for="signup-password"]').textContent = translations[lang].passwordLabel;
    document.querySelector('#signup-form label[for="signup-confirm-password"]').textContent = translations[lang].confirmPasswordLabel;
    document.querySelector('#signin-form .options label').textContent = translations[lang].rememberMe;
    document.querySelector('#signin-form .options a').textContent = translations[lang].forgotPassword;
    document.querySelector('#signin-form .btn').textContent = translations[lang].signinButton;
    document.querySelector('#signup-form .btn').textContent = translations[lang].signupButton;
    document.querySelector('#signin-username-error').textContent = translations[lang].usernameError;
    document.querySelector('#signin-password-error').textContent = translations[lang].passwordError;
    document.querySelector('#signup-email-error').textContent = translations[lang].emailError;
    document.querySelector('#signup-username-error').textContent = translations[lang].usernameError;
    document.querySelector('#signup-password-error').textContent = translations[lang].passwordError;
    document.querySelector('#confirm-password-error').textContent = translations[lang].confirmPasswordError;
    document.querySelector('#username-taken-error').textContent = translations[lang].usernameTaken;
}

// Initialize with English
updateLanguage('en');

    const API_BASE = window.location.origin;

    const btn = document.querySelector('btn scale-in');
    btn.addEventListener('click', () => {
    e.preventDefault();
})

    async function register() {
    try {
    const email = document.getElementById("signup-email").value;
    const username = document.getElementById("signup-username").value;
    const password = document.getElementById("signup-password").value;
    const confirmPassword = document.getElementById("signup-confirm-password").value;

    if (password !== confirmPassword)    { document.getElementById("confirm-password-error").style.display = 'block'; valid = false; }
    else                                 { document.getElementById("confirm-password-error").style.display = 'none'; }

    const resp = await fetch(`${API_BASE}/auth/register`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ email, username, password })
});

    if (!resp.ok) {
    const errorData = await resp.json();
    throw new Error(errorData.message || 'Registration failed');
}

    const { token } = await resp.json();
    localStorage.setItem('regToken', token);
    window.location.href = `/verify?username=${encodeURIComponent(username)}`;
} catch (err) {
    console.error('Registration error:', err);
    alert(err.message);
}
}



    const formm = document.getElementById('signup-form');
    formm.addEventListener('submit', e => {
    e.preventDefault();
    register();
});
