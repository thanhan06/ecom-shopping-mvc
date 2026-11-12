document.addEventListener("DOMContentLoaded", function () {
    const form = document.getElementById("bulkOrderForm");
    const errorList = document.getElementById("errorList");
    
    if (form) {
        // 1. KIỂM TRA TỒN KHO KHI RỜI Ô NHẬP
        form.addEventListener("focusout", async function (event) {
            if (event.target && event.target.name === "quantities") {
                const quantityInput = event.target;
                const row = quantityInput.closest("tr");
                const idInput = row.querySelector("input[name='productIds']");
                const productId = idInput.value;
                const quantity = parseInt(quantityInput.value);

                // Khách xóa trắng ô -> Không làm gì cả
                if (quantityInput.value.trim() === "") {
                    errorList.innerHTML = "";
                    return; 
                }

                if (quantity <= 0 || isNaN(quantity)) {
                    errorList.innerHTML = `<li style="color: red; font-weight: bold;">Số lượng phải là một số nguyên dương.</li>`;
                    return;
                }

                // Sửa lỗi 1: Thêm const
                try {
                    const response = await fetch(`/api/check-stock?productId=${productId}&quantity=${quantity}`);
                    const data = await response.json();
                    
                    if (!data.valid) {
                        quantityInput.focus();
                        errorList.innerHTML = `<li style="color: red; font-weight: bold;">${data.message}</li>`;
                    } else {
                        errorList.innerHTML = ""; // Xóa lỗi nếu kho hợp lệ
                    }
                } catch (error) {
                    console.error("Lỗi khi kiểm tra tồn kho:", error);
                }
            }
        });

        // 2. TỐI ƯU VÀ CHẶN SUBMIT
        form.addEventListener("submit", function (event) {
            // Sửa lỗi 3: Chặn ngay nếu trên màn hình đang hiển thị câu báo lỗi (chưa được khách sửa)
            if (errorList.innerHTML.trim() !== "") {
                event.preventDefault();
                alert("Vui lòng sửa các lỗi số lượng màu đỏ trước khi tạo đơn hàng!");
                return;
            }

            const rows = form.querySelectorAll("tbody tr");
            let totalQuantity = 0;
            
            rows.forEach((row) => {
                const idInput = row.querySelector("input[name='productIds']");
                const quantityInput = row.querySelector("input[name='quantities']");
                const quantity = parseInt(quantityInput.value);

                if (isNaN(quantity) || quantity <= 0) {
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

                // Mở khóa lại để khách tiếp tục thao tác
                rows.forEach((row) => {
                    row.querySelector('input[name="quantities"]').disabled = false;
                    row.querySelector('input[name="productIds"]').disabled = false;
                });
            }
        });
    }
});