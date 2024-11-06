document.addEventListener('DOMContentLoaded', async () => {
    const containers = document.querySelectorAll('.cms-markdown');
    if (containers && containers.length) {
        const {marked} = await import('https://cdnjs.cloudflare.com/ajax/libs/marked/14.1.3/lib/marked.esm.js');
        marked.use({
            breaks: true,
        });

        containers.forEach(container => {
            if (container instanceof HTMLElement) {
                container.innerHTML = DOMPurify.sanitize(marked.parse(container.innerText));
                container.classList.add('cms-markdown--rendered');
            }
        });
    }
});
