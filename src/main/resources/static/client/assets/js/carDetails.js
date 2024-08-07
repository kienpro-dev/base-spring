$(document).ready(function() {

    const addtionalFuctions = $('#additionalF').text().split(',');
    console.log(addtionalFuctions);
    $('.form-check-input').each(function () {
        if (addtionalFuctions.includes($(this).val())) {
            $(this).prop('checked', true);
        }
    });

    const terms = $('#terms').text().split(',');
    $('.form-check-input').each(function () {
        if (terms.includes($(this).val())) {
            $(this).prop('checked', true);
        }
    });

    let previousValue = $('#statusSelect').val();

    $('#statusSelect').on('change', function () {
        let newValue = $(this).val();
        let confirmation = confirm("Are you sure you want to change the status?");

        if (confirmation) {
            $.ajax({
                url: '/car-owner/my-car/status',
                method: 'POST',
                data: {
                    status: newValue
                },
                success: function (data) {
                    alert('Status updated successfully!');
                },
                error: function (error) {
                    alert('Error updating status!');
                    $('#statusSelect').val(previousValue);
                }

            })
            previousValue = newValue;
        } else {
            $('#statusSelect').val(previousValue);
        }
    });
});