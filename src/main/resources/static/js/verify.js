const API_BASE = window.location.origin;
const params = new URLSearchParams(window.location.search);
const username = params.get('username');
let attempts = 0;

async function submitCode(code) {
    try {
        const token = localStorage.getItem('regToken');
        if (!token) throw new Error('Session expired. Please register again.');

        const resp = await fetch(`${API_BASE}/auth/verify`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            },
            body: JSON.stringify({ code })
        });

        if (resp.ok) {
            const data = await resp.json();
            localStorage.setItem('authToken', data.token);
            window.location.href = '/dashboard';
        } else {
            const error = await resp.json();
            throw new Error(error.message);
        }
    } catch (err) {
        console.error('Verification error:', err);
        errorElement.textContent = err.message;
        errorElement.style.display = 'block';
        if (++attempts >= 3) {
            alert('Too many failed attempts. Please register again.');
            window.location.href = '/auth';
        }
    }
}
async function resendCode() {
    try {
        const token = localStorage.getItem('regToken');
        const resp = await fetch(`${API_BASE}/auth/resend`, {
            method: 'POST',
            headers: {
                'Authorization': `Bearer ${token}`
            }
        });

        if (!resp.ok) throw new Error('Failed to resend code');
        alert('New code sent successfully!');
    } catch (err) {
        console.error('Resend error:', err);
        alert(err.message);
    }
}
const translations = {
    en: {
        title: "Verify Your Account",
        subtitle: "Enter the 6-digit code sent to your email",
        verifyButton: "Verify",
        codeError: "Invalid code. Please try again.",
        resendText: "Didn't receive a code?",
        resendLink: "Resend"
    },
    ru: {
        title: "Подтвердите аккаунт",
        subtitle: "Введите 6-значный код, отправленный на вашу почту",
        verifyButton: "Подтвердить",
        codeError: "Неверный код. Попробуйте снова.",
        resendText: "Не получили код?",
        resendLink: "Отправить снова"
    },
    kz: {
        title: "Есептік жазбаңызды растаңыз",
        subtitle: "Электрондық поштаңызға жіберілген 6 таңбалы кодты енгізіңіз",
        verifyButton: "Растау",
        codeError: "Код қате. Қайтадан көріңіз.",
        resendText: "Код алмадыңыз ба?",
        resendLink: "Қайта жіберу"
    }
};

let currentLang = 'en';

// Language Switching
function updateLanguage(lang) {
    currentLang = lang;
    document.querySelector('h2').textContent = translations[lang].title;
    document.querySelector('p').textContent = translations[lang].subtitle;
    document.querySelector('.btn').textContent = translations[lang].verifyButton;
    document.querySelector('#code-error').textContent = translations[lang].codeError;
    document.querySelector('.resend').childNodes[0].textContent = translations[lang].resendText + ' ';
    document.querySelector('#resend-link').textContent = translations[lang].resendLink;
}

updateLanguage('en');

const inputs = document.querySelectorAll('.code-input');
const submitButton = document.querySelector('.btn');
const form = document.getElementById('verify-form');
const errorElement = document.getElementById('code-error');

inputs.forEach((input, index) => {
    input.addEventListener('input', (e) => {
        if (e.target.value.length === 1 && index < inputs.length - 1) {
            inputs[index + 1].focus();
        }
        checkInputs();
    });

    input.addEventListener('keydown', (e) => {
        if (e.key === 'Backspace' && !e.target.value && index > 0) {
            inputs[index - 1].focus();
        }
    });

    input.addEventListener('paste', (e) => {
        e.preventDefault();
        const paste = e.clipboardData.getData('text').replace(/\D/g, '');
        if (paste.length <= 6) {
            paste.split('').forEach((char, i) => {
                if (i < inputs.length) {
                    inputs[i].value = char;
                }
            });
            inputs[Math.min(paste.length, inputs.length - 1)].focus();
            checkInputs();
        }
    });
});

function checkInputs() {
    const allFilled = Array.from(inputs).every(input => input.value.length === 1);
    submitButton.disabled = !allFilled;
}

form.addEventListener('submit', async (e) => {
    e.preventDefault();
    const code = Array.from(inputs).map(input => input.value).join('');
    if (code.length === 6) {
        try {
            const response = await fetch('/api/verify', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ code })
            });
            if (response.ok) {
                window.location.href = '/dashboard.html';
            } else {
                errorElement.style.display = 'block';
                inputs.forEach(input => input.value = '');
                submitButton.disabled = true;
            }
        } catch (error) {
            console.error('Error:', error);
            errorElement.style.display = 'block';
            inputs.forEach(input => input.value = '');
            submitButton.disabled = true;
        }
    }
});

document.getElementById('resend-link').addEventListener('click', async (e) => {
    e.preventDefault();
    try {
        const response = await fetch('/api/resend-code', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' }
        });
        if (response.ok) {
            alert('Code resent successfully!');
        } else {
            alert('Failed to resend code. Please try again.');
        }
    } catch (error) {
        console.error('Error:', error);
        alert('An error occurred. Please try again.');
    }
});