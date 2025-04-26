const translations = {
    en: {
        language: "Language",
        profile: "Profile",
        logout: "Log Out",
        recommend: "View My Recommendations",
        myLikes: "My Likes",
        recentlyViewed: "Recently Viewed",
        discoveryWeek: "Discovery of the Week",
        footerTitle: "Ready to Meme?",
        contactUs: "Contact Us",
        subscribe: "Subscribe",
        email: "Email: contact@memigma.com",
        address: "Address: 123 Meme Street, Internet City",
        phone: "Phone: +1 (555) 123-4567"
    },
    ru: {
        language: "Язык",
        profile: "Профиль",
        logout: "Выйти",
        recommend: "Посмотреть мои рекомендации",
        myLikes: "Мои лайки",
        recentlyViewed: "Недавно просмотренные",
        discoveryWeek: "Открытие недели",
        footerTitle: "Готовы к мемам?",
        contactUs: "Связаться с нами",
        subscribe: "Подписаться",
        email: "Email: contact@memigma.com",
        address: "Адрес: ул. Мемов, 123, Интернет-Сити",
        phone: "Телефон: +1 (555) 123-4567"
    },
    kz: {
        language: "Тіл",
        profile: "Профиль",
        logout: "Шығу",
        recommend: "Менің ұсыныстарымды қарау",
        myLikes: "Менің ұнатқандарым",
        recentlyViewed: "Жақында қаралған",
        discoveryWeek: "Аптаның ашылуы",
        footerTitle: "Мемдерге дайынсыз ба?",
        contactUs: "Бізбен байланыс",
        subscribe: "Жазылу",
        email: "Email: contact@memigma.com",
        address: "Мекенжай: Мемдер көшесі, 123, Интернет қаласы",
        phone: "Телефон: +1 (555) 123-4567"
    }
};

let currentLang = 'en';

function sendRequest(endpoint) {
    fetch(endpoint, { method: 'POST' })
        .then(response => response.json())
        .then(data => console.log('Request sent:', data))
        .catch(error => console.error('Error:', error));
}

function logout() {
    fetch('/api/logout', { method: 'POST' })
        .then(response => {
            if (response.ok) {
                window.location.href = '/index.html';
            } else {
                console.error('Logout failed');
            }
        })
        .catch(error => console.error('Error:', error));
}

function changeLanguage(lang) {
    currentLang = lang;
    document.querySelector('.lang-btn').textContent = translations[lang].language;
    document.querySelector('.profile-btn').textContent = translations[lang].profile;
    document.querySelector('.profile-options button').textContent = translations[lang].logout;
    document.querySelector('.recommend-btn').textContent = translations[lang].recommend;
    document.querySelectorAll('.right-sidebar h3')[0].textContent = translations[lang].myLikes;
    document.querySelectorAll('.right-sidebar h3')[1].textContent = translations[lang].recentlyViewed;
    document.querySelectorAll('.right-sidebar h3')[2].textContent = translations[lang].discoveryWeek;
    document.querySelector('footer h2').textContent = translations[lang].footerTitle;
    document.querySelectorAll('.footer-btn')[0].textContent = translations[lang].contactUs;
    document.querySelectorAll('.footer-btn')[1].textContent = translations[lang].subscribe;
    document.querySelectorAll('.footer-contact p')[0].textContent = translations[lang].email;
    document.querySelectorAll('.footer-contact p')[1].textContent = translations[lang].address;
    document.querySelectorAll('.footer-contact p')[2].textContent = translations[lang].phone;
}

// Dropdown Handling
document.querySelector('.lang-btn').addEventListener('click', () => {
    const options = document.querySelector('.lang-options');
    options.style.display = options.style.display === 'flex' ? 'none' : 'flex';
});

document.querySelector('.profile-btn').addEventListener('click', () => {
    const options = document.querySelector('.profile-options');
    options.style.display = options.style.display === 'flex' ? 'none' : 'flex';
});

document.addEventListener('click', (e) => {
    if (!e.target.closest('.lang-dropdown')) {
        document.querySelector('.lang-options').style.display = 'none';
    }
    if (!e.target.closest('.profile-dropdown')) {
        document.querySelector('.profile-options').style.display = 'none';
    }
});

// Scroll Animations
window.addEventListener('scroll', () => {
    document.querySelectorAll('.fade-in').forEach(el => {
        const rect = el.getBoundingClientRect();
        if (rect.top < window.innerHeight && rect.bottom > 0) {
            el.style.opacity = '1';
        }
    });
});

// Initialize Language
changeLanguage('en');