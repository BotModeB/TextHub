(() => {
    const nav = document.querySelector('.secondary-nav');
    if (!nav) return;

    let lastY = window.scrollY || 0;
    const delta = 8;

    const hide = () => nav.classList.add('is-hidden');
    const show = () => nav.classList.remove('is-hidden');

    window.addEventListener('scroll', () => {
        const y = window.scrollY || 0;
        if (Math.abs(y - lastY) < delta) return;

    if (y > lastY && y > 80) {
        hide();
    } else {
        show();
    }
    lastY = y;
    }, { passive: true });
})();
