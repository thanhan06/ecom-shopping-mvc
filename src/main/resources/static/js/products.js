
document.addEventListener("DOMContentLoaded", function () {

    const form = document.getElementById("bulkOrderForm");

    // Lưu lỗi theo từng productId
    const errors = new Map();

    // Cập nhật Toast từ danh sách lỗi
    function updateErrors() {
        Notification.showErrors([...errors.values()]);
    }

    if (form) {

        // KIỂM TRA TỒN KHO KHI RỜI Ô NHẬP
        form.addEventListener("focusout", async function (event) {

            if (event.target?.name !== "quantities") return;

            const quantityInput = event.target;
            const row = quantityInput.closest("tr");
            const idInput = row.querySelector("input[name='productIds']");

            const productId = idInput.value;
            const value = quantityInput.value.trim();

            // Nếu để trống: xóa lỗi của riêng sản phẩm này
            if (value === "") {
                errors.delete(productId);
                updateErrors();
                return;
            }

            const quantity = Number(value);

            if (!Number.isInteger(quantity) || quantity <= 0) {
                errors.set(
                    productId,
                    "Số lượng phải là một số nguyên dương."
                );

                updateErrors();
                return;
            }

            try {
                const response = await fetch(
                    `/api/check-stock?productId=${encodeURIComponent(productId)}&quantity=${quantity}`
                );

                if (!response.ok) {
                    throw new Error("Không thể kiểm tra tồn kho.");
                }

                const data = await response.json();

                if (!data.valid) {
                    errors.set(productId, data.message);
                    quantityInput.focus(); // chặn không cho nhập số lượng không hợp lệ cho sản phẩm tiếp theo khi chưa sửa lỗi
                } else {
                    errors.delete(productId);
                }

                updateErrors();

            } catch (error) {
                console.error("Lỗi khi kiểm tra tồn kho:", error);

                errors.set(
                    productId,
                    "Không thể kiểm tra tồn kho. Vui lòng thử lại."
                );

                updateErrors();
            }
        });

        // CHẶN SUBMIT
        form.addEventListener("submit", function (event) {

            // Kiểm tra các lỗi đã ghi nhận
            if (errors.size > 0) {
                event.preventDefault();
                updateErrors();
                return;
            }

            const rows = form.querySelectorAll("tbody tr");
            let totalQuantity = 0;

            rows.forEach(row => {
                const quantityInput = row.querySelector(
                    "input[name='quantities']"
                );

                const quantity = Number(quantityInput.value);

                if (Number.isInteger(quantity) && quantity > 0) {
                    totalQuantity += quantity;
                }
            });

            if (totalQuantity === 0) {
                event.preventDefault();
                console.log("Không có sản phẩm nào được chọn.");
                Notification.showError(
                    "Hãy chọn ít nhất 1 sản phẩm."
                );

                return;
            }

            // Chỉ gửi những sản phẩm có số lượng hợp lệ
            rows.forEach(row => {
                const idInput = row.querySelector(
                    "input[name='productIds']"
                );

                const quantityInput = row.querySelector(
                    "input[name='quantities']"
                );

                const quantity = Number(quantityInput.value);

                const valid = Number.isInteger(quantity) && quantity > 0;

                idInput.disabled = !valid;
                quantityInput.disabled = !valid;
            });
        });
    }
});
