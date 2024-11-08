document.addEventListener('DOMContentLoaded', async () => {
    const containers = document.querySelectorAll('.cms-markdown');
    if (containers && containers.length) {
        const {
            marked,
            Renderer
        } = await import('https://cdnjs.cloudflare.com/ajax/libs/marked/14.1.3/lib/marked.esm.js');

        // Custom renderers for primefaces style
        const renderer = new Renderer();
        renderer.link = ({href, text, title}) => {
            const titleAttr = title ? ` title="${title}"` : '';
            return `<a href="${href}" class="ui-link ui-widget"${titleAttr}>${text}</a>`;
        };

        marked.use({
            breaks: true,
            renderer,
        });

        containers.forEach(container => {
            if (container instanceof HTMLElement) {
                container.innerHTML = DOMPurify.sanitize(marked.parse(container.innerText));
                container.classList.add('cms-markdown--rendered');
            }
        });
    }
});
