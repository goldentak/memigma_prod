function navigateToAuth() {
    fetch('/auth/login', {
        method: 'GET',
        headers: { 'Accept': 'text/html' }
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Failed to load login page');
            }
            return response.text();
        })
        .then(html => {
            document.open();
            document.write(html);
            document.close();
        })
        .catch(err => {
            console.error(err);
            window.location.href = '/login';
        });
}

var gk_isXlsx = false;
var gk_xlsxFileLookup = {};
var gk_fileData = {};
function filledCell(cell) {
    return cell !== '' && cell != null;
}
function loadFileData(filename) {
    if (gk_isXlsx && gk_xlsxFileLookup[filename]) {
        try {
            var workbook = XLSX.read(gk_fileData[filename], { type: 'base64' });
            var firstSheetName = workbook.SheetNames[0];
            var worksheet = workbook.Sheets[firstSheetName];

            // Convert sheet to JSON to filter blank rows
            var jsonData = XLSX.utils.sheet_to_json(worksheet, { header: 1, blankrows: false, defval: '' });
            // Filter out blank rows (rows where all cells are empty, null, or undefined)
            var filteredData = jsonData.filter(row => row.some(filledCell));

            // Heuristic to find the header row by ignoring rows with fewer filled cells than the next row
            var headerRowIndex = filteredData.findIndex((row, index) =>
                row.filter(filledCell).length >= filteredData[index + 1]?.filter(filledCell).length
            );
            // Fallback
            if (headerRowIndex === -1 || headerRowIndex > 25) {
                headerRowIndex = 0;
            }

            // Convert filtered JSON back to CSV
            var csv = XLSX.utils.aoa_to_sheet(filteredData.slice(headerRowIndex)); // Create a new sheet from filtered array of arrays
            csv = XLSX.utils.sheet_to_csv(csv, { header: 1 });
            return csv;
        } catch (e) {
            console.error(e);
            return "";
        }
    }
    return gk_fileData[filename] || "";
}

const translations = {
    en: {
        login: "LOGIN",
        language: "LANGUAGE",
        getStarted: "GET STARTED",
        aboutTitle: "About Memigma",
        aboutText: "Memigma is the ultimate Tinder for memes! Swipe through hilarious content, discover new favorites, and share the laughter with your friends.",
        howTitle: "How It Works",
        howText: "Sign up, swipe left or right on memes, and build your personal meme collection. It's fun, addictive, and packed with humor!",
        communityTitle: "Join the Community",
        communityText: "Connect with meme lovers worldwide, create your own memes, and compete for the top spot on the leaderboard.",
        footerTitle: "Ready to Meme?",
        contactUs: "CONTACT US",
        subscribe: "SUBSCRIBE",
        email: "Email: contact@memigma.com",
        address: "Address: 123 Meme Street, Internet City",
        phone: "Phone: +1 (555) 123-4567"
    },
    ru: {
        login: "ВОЙТИ",
        language: "ЯЗЫК",
        getStarted: "НАЧАТЬ",
        aboutTitle: "О Memigma",
        aboutText: "Memigma - это Tinder для мемов! Листайте смешной контент, находите новые любимые мемы и делитесь смехом с друзьями.",
        howTitle: "Как это работает",
        howText: "Зарегистрируйтесь, листайте мемы влево или вправо и создавайте свою личную коллекцию мемов. Это весело, захватывающе и полно юмора!",
        communityTitle: "Присоединяйтесь к сообществу",
        communityText: "Общайтесь с любителями мемов по всему миру, создавайте свои мемы и соревнуйтесь за первое место в рейтинге.",
        footerTitle: "Готовы к мемам?",
        contactUs: "СВЯЗАТЬСЯ С НАМИ",
        subscribe: "ПОДПИСАТЬСЯ",
        email: "Email: contact@memigma.com",
        address: "Адрес: ул. Мемов, 123, Интернет-Сити",
        phone: "Телефон: +1 (555) 123-4567"
    },
    kz: {
        login: "КІРУ",
        language: "ТІЛ",
        getStarted: "БАСТАУ",
        aboutTitle: "Memigma туралы",
        aboutText: "Memigma - мемдерге арналған Tinder! Күлкілі контентті айналдырыңыз, жаңа сүйікті мемдерді табыңыз және достарыңызбен күлкіні бөлісіңіз.",
        howTitle: "Бұл қалай жұмыс істейді",
        howText: "Тіркеліңіз, мемдерді солға немесе оңға айналдырыңыз және жеке мемдер коллекцияңызды жасаңыз. Бұл қызықты, әсерлі және юморға толы!",
        communityTitle: "Қауымдастыққа қосылыңыз",
        communityText: "Әлемдегі мемдерді ұнататынлармен байланысыңыз, өз мемдеріңізді жасаңыз және рейтингте бірінші орын үшін бәсекелесіңіз.",
        footerTitle: "Мемдерге дайынсыз ба?",
        contactUs: "БІЗБЕН БАЙЛАНЫС",
        subscribe: "ЖАЗЫЛУ",
        email: "Email: contact@memigma.com",
        address: "Мекенжай: Мемдер көшесі, 123, Интернет қаласы",
        phone: "Телефон: +1 (555) 123-4567"
    }
}
    function changeLanguage(lang) {
    document.querySelector('.login-btn').textContent = translations[lang].login;
    document.querySelector('.lang-btn').textContent = translations[lang].language;
    document.querySelector('.get-started').textContent = translations[lang].getStarted;
    document.querySelector('.section1 h2').textContent = translations[lang].aboutTitle;
    document.querySelector('.section1 p').textContent = translations[lang].aboutText;
    document.querySelector('.section2 h2').textContent = translations[lang].howTitle;
    document.querySelector('.section2 p').textContent = translations[lang].howText;
    document.querySelector('.section3 h2').textContent = translations[lang].communityTitle;
    document.querySelector('.section3 p').textContent = translations[lang].communityText;
    document.querySelector('footer h2').textContent = translations[lang].footerTitle;
    document.querySelectorAll('.footer-btn')[0].textContent = translations[lang].contactUs;
    document.querySelectorAll('.footer-btn')[1].textContent = translations[lang].subscribe;
    document.querySelectorAll('.footer-contact p')[0].textContent = translations[lang].email;
    document.querySelectorAll('.footer-contact p')[1].textContent = translations[lang].address;
    document.querySelectorAll('.footer-contact p')[2].textContent = translations[lang].phone;
}

document.querySelector('.lang-btn').addEventListener('click', () => {
    const options = document.querySelector('.lang-options');
    options.style.display = options.style.display === 'flex' ? 'none' : 'flex';
});

document.addEventListener('click', (e) => {
    if (!e.target.closest('.lang-dropdown')) {
        document.querySelector('.lang-options').style.display = 'none';
    }
});

// Trigger animations on scroll
window.addEventListener('scroll', () => {
    document.querySelectorAll('.fade-in').forEach(el => {
        const rect = el.getBoundingClientRect();
        if (rect.top < window.innerHeight && rect.bottom > 0) {
            el.style.opacity = '1';
        }
    });
});


function sendRequest(endpoint) {
    fetch(endpoint, { method: 'POST' })
        .then(response => response.json())
        .then(data => console.log('Request sent:', data))
        .catch(error => console.error('Error:', error));
}

function navigateToAuth() {
    fetch('/api/login', { method: 'POST' })
        .then(response => {
            if (response.ok) {
                window.location.href = 'auth.html';
            } else {
                console.error('Login request failed');
            }
        })
        .catch(error => console.error('Error:', error));

};