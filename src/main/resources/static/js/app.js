document.addEventListener("DOMContentLoaded", () => {
    const revealTargets = document.querySelectorAll("[data-reveal]");

    if (!revealTargets.length) {
        return;
    }

    const prefersReducedMotion = window.matchMedia("(prefers-reduced-motion: reduce)").matches;

    if (prefersReducedMotion) {
        revealTargets.forEach((element) => element.classList.add("is-visible"));
        return;
    }

    // 화면에 처음 보이는 요소는 즉시 노출해 첫인상이 지연되지 않도록 합니다.
    const revealImmediately = () => {
        revealTargets.forEach((element) => {
            const rect = element.getBoundingClientRect();
            if (rect.top < window.innerHeight * 0.92) {
                element.classList.add("is-visible");
            }
        });
    };

    revealImmediately();

    // 스크롤 진입 시 카드가 부드럽게 나타나도록 관찰자를 사용합니다.
    const observer = new IntersectionObserver(
        (entries) => {
            entries.forEach((entry) => {
                if (entry.isIntersecting) {
                    entry.target.classList.add("is-visible");
                    observer.unobserve(entry.target);
                }
            });
        },
        {
            threshold: 0.18,
            rootMargin: "0px 0px -30px 0px"
        }
    );

    revealTargets.forEach((element) => observer.observe(element));
});
