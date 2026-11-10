document.addEventListener("DOMContentLoaded", function () {
    const form = document.getElementById("bulkOrderForm");
    const messageContainer = document.getElementById("messageContainer");
    const errorList = document.getElementById("errorList");
    if (form) {
        // UNFOCUS INPUTS
        form.addEventListener("focusout", async function (event) {
            if (event.target && event.target.name === "quantities") {
                const quantityInput = event.target;
                const row = quantityInput.closest("tr");
                const idInput = row.querySelector("input[name='productIds']");
                const productId = idInput.value;
                const quantity = parseInt(quantityInput.value) || 0;

                console.log(quantityInput);

                if (quantityInput.value.trim() !== "" && (quantity < 0 || isNaN(quantity))) {
                    errorList.innerHTML = `<li style="color: red; font-weight: bold;">Số lượng phải là một số nguyên dương.</li>`;
                    return;
                } else {
                    errorList.innerHTML = ""; // Clear previous error messages
                }

                try {
                    response = await fetch(`/api/check-stock?productId=${productId}&quantity=${quantity}`);
                    const data = await response.json();
                    if (!data.valid) {
                        errorList.innerHTML = `<li style="color: red; font-weight: bold;">${data.message}</li>`;
                        quantityInput.focus();
                    }

                } catch (error) {
                    console.error("Error during focusout event:", error);
                }
            }
        });
        //SUBMIT FORM
        form.addEventListener("submit", function (event) {
            const rows = form.querySelectorAll("tbody tr");
            let totalQuantity = 0;
            console.log(rows);
            rows.forEach((row) => {
                const idInput = row.querySelector("input[name='productIds']");
                const quantityInput = row.querySelector("input[name='quantities']");
                const quantity = parseInt(quantityInput.value);

                if (quantity <= 0 || isNaN(quantity)) {
                    idInput.disabled = true;
                    quantityInput.disabled = true;
                } else {
                    idInput.disabled = false;
                    quantityInput.disabled = false;
                    totalQuantity += quantity;
                }
            });
            if (totalQuantity === 0) {
                event.preventDefault();
                errorList.innerHTML = `<li style="color: red; font-weight: bold;">Hãy chọn ít nhất 1 sản phẩm.</li>`;

                // Mở khóa lại toàn bộ input để khách có thể tiếp tục thao tác
                rows.forEach((row) => {
                    row.querySelector('input[name="quantities"]').disabled = false;
                    row.querySelector('input[name="productIds"]').disabled = false;
                });
            }
        });
    }


});
