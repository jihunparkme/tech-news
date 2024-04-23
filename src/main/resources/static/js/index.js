document.addEventListener('DOMContentLoaded', function() {
    const checkboxes = document.querySelectorAll('input[type="checkbox"]');
    checkboxes.forEach(chk => {
        chk.addEventListener('change', function() {
            handleCheckboxChange();
        });
    });

    function handleCheckboxChange() {
        const checkedBoxes = document.querySelectorAll('input[type="checkbox"]:checked');
        let selectedItems = [];
        checkedBoxes.forEach(chk => {
            selectedItems.push(chk.value);
        });
        console.log("선택된 항목들:", selectedItems.join(', '));
    }
});