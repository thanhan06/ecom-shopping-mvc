document.addEventListener('DOMContentLoaded', function() {
    const form = document.getElementById('bulkOrderForm');
    
    if (form) {
        // 1. KIỂM TRA TỒN KHO KHI RỜI Ô NHẬP (Event Delegation bằng focusout)
        form.addEventListener('focusout', async function(event) {
            
            // Chỉ xử lý nếu phần tử vừa tương tác là ô nhập số lượng
            if (event.target && event.target.name === 'quantities') {
                const input = event.target;
                const row = input.closest('tr'); // Lấy thẻ <tr> chứa ô input
                
                // Lấy ID sản phẩm tương ứng cùng dòng
                const idInput = row.querySelector('input[name="productIds"]');
                const productId = idInput.value;
                
                // Tìm hoặc tạo thẻ span để hiển thị lỗi ngay dưới ô nhập
                let errorSpan = row.querySelector('.qty-error');
                if (!errorSpan) {
                    errorSpan = document.createElement('span');
                    errorSpan.className = 'qty-error';
                    errorSpan.style.color = 'red';
                    errorSpan.style.fontSize = '0.85em';
                    errorSpan.style.display = 'block';
                    input.parentNode.appendChild(errorSpan);
                }

                // Tiền xử lý dữ liệu nhập
                let val = parseInt(input.value);
                
                // Nếu khách xóa trắng hoặc nhập số âm, tự động làm sạch ô và bỏ qua check
                if (input.value.trim() === '' || isNaN(val) || val <= 0) {
                    input.value = '';
                    errorSpan.textContent = ''; // Xóa cảnh báo
                    return; 
                }

                // Hiển thị trạng thái đang tải (tùy chọn)
                errorSpan.style.color = 'blue';
                errorSpan.textContent = 'Đang kiểm tra...';

                try {
                    // Gọi API kiểm tra tồn kho ở Backend
                    const response = await fetch(`/api/check-stock?productId=${productId}&quantity=${val}`);
                    const data = await response.json();

                    if (!data.valid) {
                        // Hiển thị lỗi màu đỏ nếu vượt quá tồn kho
                        errorSpan.style.color = 'red';
                        errorSpan.textContent = data.message;
                        
                        // Tự động gán lại bằng số lượng tồn kho tối đa
                        if (data.actualStock !== undefined) {
                            input.value = data.actualStock;
                        }
                    } else {
                        // Xóa lỗi nếu hợp lệ
                        errorSpan.textContent = ''; 
                    }
                } catch (error) {
                    console.error("Lỗi khi kiểm tra tồn kho:", error);
                    errorSpan.style.color = 'red';
                    errorSpan.textContent = 'Lỗi kết nối!';
                }
            }
        });

        // 2. TỐI ƯU DỮ LIỆU TRƯC KHI SUBMIT TẠO ĐƠN HÀNG
        form.addEventListener('submit', function(event) {
            const rows = form.querySelectorAll('tbody tr');
            let totalItems = 0;
            
            rows.forEach(row => {
                const qtyInput = row.querySelector('input[name="quantities"]');
                const idInput = row.querySelector('input[name="productIds"]');
                const val = parseInt(qtyInput.value);
                
                // Nếu khách KHÔNG mua món này (ô trống, nhập số 0, hoặc số âm)
                if (isNaN(val) || val <= 0) {
                    // Vô hiệu hóa thẻ input để trình duyệt KHÔNG gửi món này lên Server
                    qtyInput.disabled = true;
                    idInput.disabled = true;
                } else {
                    // Mở khóa những món hợp lệ
                    qtyInput.disabled = false;
                    idInput.disabled = false;
                    totalItems += val;
                }
            });
            
            // Chặn gửi form nếu tổng số lượng bằng 0 (khách chưa chọn mua gì)
            if (totalItems === 0) {
                event.preventDefault(); // Dừng hành động submit
                alert('Vui lòng chọn ít nhất 1 sản phẩm trước khi tạo đơn hàng!');
                
                // Mở khóa lại toàn bộ input để khách có thể tiếp tục thao tác
                rows.forEach(row => {
                    row.querySelector('input[name="quantities"]').disabled = false;
                    row.querySelector('input[name="productIds"]').disabled = false;
                });
            }
        });
    }
});