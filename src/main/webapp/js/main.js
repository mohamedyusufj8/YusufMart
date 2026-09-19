/**
 * YusufMart Client Script
 * Vanilla JS enhancements for interactivity
 */

document.addEventListener('DOMContentLoaded', () => {
    // Auto-dismiss success alerts after 4 seconds
    const alerts = document.querySelectorAll('.alert-success');
    alerts.forEach(alert => {
        setTimeout(() => {
            alert.style.transition = 'opacity 0.5s ease';
            alert.style.opacity = '0';
            setTimeout(() => alert.remove(), 500);
        }, 4000);
    });

    // Format credit card input with dashes automatically
    const cardInput = document.getElementById('accountIdentifier');
    if (cardInput) {
        cardInput.addEventListener('input', (e) => {
            let val = e.target.value.replace(/\D/g, '');
            let newVal = '';
            for (let i = 0; i < val.length; i++) {
                if (i > 0 && i % 4 === 0 && i < 16) {
                    newVal += '-';
                }
                newVal += val[i];
            }
            e.target.value = newVal.substring(0, 19);
        });
    }
});
