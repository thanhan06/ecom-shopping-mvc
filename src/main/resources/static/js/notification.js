const Notification = {
  errorToast: null,
  errorList: null,
  successToast: null,

  // Khởi tạo
  init() {
    const errorToastElement = document.getElementById("errorToast");
    const successToastElement = document.getElementById("successToast");

    this.errorList = document.getElementById("errorList");

    if (errorToastElement) {
      this.errorToast = bootstrap.Toast.getOrCreateInstance(errorToastElement, {
        delay: 3000,
        autohide: true,
      });
    }

    if (successToastElement) {
      this.successToast = bootstrap.Toast.getOrCreateInstance(
        successToastElement,
        { delay: 3000, autohide: true },
      );
    }
  },

  // Hiển thị lỗi
  showError(message) {
    if (!this.errorToast || !this.errorList) return;

    this.errorList.textContent = "";

    const li = document.createElement("li");
    li.textContent = message;

    this.errorList.appendChild(li);

    this.errorToast.show();
  },
  // Hiển thị lỗi
  showErrors(messages) {
    if (!this.errorToast || !this.errorList) return;

    this.errorList.textContent = "";

    messages.forEach((message) => {
      const li = document.createElement("li");
      li.textContent = message;
      this.errorList.appendChild(li);
    });

    if (messages.length > 0) {
      this.errorToast.show();
    } else {
      this.errorToast.hide();
    }
  },

  // Hiển thị thành công
  showSuccess(message) {
    if (!this.successToast) return;

    const body = this.successToast.getElement().querySelector(".toast-body");

    body.textContent = message;

    this.successToast.show();
  },

  // Xóa thông báo lỗi
  clearError() {
    if (!this.errorToast || !this.errorList) return;

    this.errorList.textContent = "";
    this.errorToast.hide();
  },
};

// Khởi tạo sau khi DOM được tải
document.addEventListener("DOMContentLoaded", () => {
  Notification.init();
});
