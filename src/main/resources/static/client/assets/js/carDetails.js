$(document).ready(function() {
    if( $('#additionalF').text() !== ''){
        const addtionalFuctions = $('#additionalF').text().split(',');
        $('.form-check-input').each(function () {
            if (addtionalFuctions.includes($(this).val())) {
                $(this).prop('checked', true);
            }
        });
    }

    const terms = $('#terms').text().split(',');
    $('.form-check-input').each(function () {
        if (terms.includes($(this).val())) {
            $(this).prop('checked', true);
        }
    });

    let previousValue = $('#statusSelect').val(); // Initialize with the current value

    $('#statusSelect').on('change', function () {
        let newValue = $(this).val();
        let confirmation = confirm("Are you sure you want to change the status?");
        let carId = $('#id').val();
        console.log('carId' + carId);
        if (confirmation) {
            $.ajax({
                url: '/car-owner/my-car/status/' + carId,
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                data: JSON.stringify({
                    status: newValue
                }),
                success: function (data) {
                    alert('Status updated successfully!');
                    previousValue = newValue; // Update the previousValue only on successful update
                    location.href = '/car-owner/my-car/';
                },
                error: function (error) {
                    alert('Error updating status!');
                    $('#statusSelect').val(previousValue); // Revert to previous value on error
                }
            });
        } else {
            $('#statusSelect').val(previousValue); // Revert to previous value if not confirmed
        }
    });

});