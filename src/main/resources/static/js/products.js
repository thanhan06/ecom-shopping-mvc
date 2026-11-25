document.addEventListener("DOMContentLoaded", function () {
  const form = document.getElementById("bulkOrderForm");

  // Lưu lỗi theo từng productId
  const errors = new Map();

  // Cập nhật Toast từ danh sách lỗi
  function updateErrors() {
    Notification.showErrors([...errors.values()]);
  }
  console.log("form", form);
  if (form) {
    // KIỂM TRA TỒN KHO KHI RỜI Ô NHẬP
    form.addEventListener("focusout", async function (event) {
      if (!event.target?.name?.endsWith(".quantity")) return;

      const quantityInput = event.target;
      console.log("quantityInput", quantityInput);
      const row = quantityInput.closest("tr");
      const idInput = row.querySelector(".product-id-input");
      console.log("idInput", idInput);
      const productId = idInput.value;
      const value = quantityInput.value.trim();

      // Nếu để trống: xóa lỗi của riêng sản phẩm này
      if (value === "") {
        errors.delete(productId);

        try {
          await updateCart(productId, 0);
        } catch (error) {
          console.error("Lỗi khi xóa sản phẩm khỏi giỏ hàng:", error);
          errors.set(productId, "Không thể cập nhật giỏ hàng.");
        }

        updateErrors();
        return;
      }

      const quantity = Number(value);

      if (!Number.isInteger(quantity) || quantity <= 0) {
        errors.set(productId, "Số lượng phải là một số nguyên dương.");

        updateErrors();
        return;
      }
      let data;
      try {
        const response = await fetch(
          `/api/check-stock?productId=${encodeURIComponent(productId)}&quantity=${quantity}`,
        );

        if (!response.ok) {
          throw new Error("Không thể kiểm tra tồn kho.");
        }

        data = await response.json();
      } catch (error) {
        console.error("Lỗi khi kiểm tra tồn kho:", error);

        errors.set(productId, "Không thể kiểm tra tồn kho. Vui lòng thử lại.");

        updateErrors();
        return;
      }
      if (!data.valid) {
        errors.set(productId, data.message);
        quantityInput.focus();
        updateErrors();
        return;
      }
      // Chỉ lưu khi tồn kho hợp lệ
      try {
        await updateCart(productId, quantity);
        errors.delete(productId);
      } catch (error) {
        console.error(error);
        errors.set(productId, "Không thể lưu giỏ hàng.");
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

      rows.forEach((row) => {
        const quantityInput = row.querySelector(".quantity-input");

        const quantity = Number(quantityInput.value);

        if (Number.isInteger(quantity) && quantity > 0) {
          totalQuantity += quantity;
        }
      });

      if (totalQuantity === 0) {
        event.preventDefault();
        console.log("Không có sản phẩm nào được chọn.");
        Notification.showError("Hãy chọn ít nhất 1 sản phẩm.");

        return;
      }

      // Chỉ gửi những sản phẩm có số lượng hợp lệ
      rows.forEach((row) => {
        const idInput = row.querySelector(".product-id-input");
        const quantityInput = row.querySelector(".quantity-input");

        const quantity = Number(quantityInput.value);

        const valid = Number.isInteger(quantity) && quantity > 0;

        idInput.disabled = !valid;
        quantityInput.disabled = !valid;
      });
    });
  }
});

async function updateCart(productId, quantity) {
  const params = new URLSearchParams();

  params.append("productId", productId);
  params.append("quantity", quantity);

  // Lấy CSRF token từ HTML
  const csrfToken = document.querySelector('meta[name="_csrf"]').content;

  const csrfHeader = document.querySelector(
    'meta[name="_csrf_header"]',
  ).content;

  const response = await fetch("/cart/update", {
    method: "POST",
    headers: {
      "Content-Type": "application/x-www-form-urlencoded",
      [csrfHeader]: csrfToken,
    },
    body: params,
  });

  if (!response.ok) {
    throw new Error("Không thể lưu giỏ hàng.");
  }
}
