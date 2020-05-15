jQuery(function($) {
    $('#monthpicker').datepicker({
        dateFormat: 'MM yy',
        showButtonPanel: true,
        beforeShow: function (input, inst) {
            setMyDate(inst);
        },
        onClose: function (dateText, inst) {
            saveMyDate(inst);
        }
    });
})

function saveMyDate(inst) {
    inst.selectedDay = 1;
    inst.input.data('year', inst.selectedYear);
    inst.input.data('month', inst.selectedMonth);
    inst.input.data('day', inst.selectedDay );

    var date = new Date(inst.selectedYear, inst.selectedMonth, inst.selectedDay);
    inst.input.datepicker('setDate', date );
    formatDate(inst, date);
    inst.input.data('date-setted', true);

    $('#maand').val(inst.selectedMonth+1)
    $('#jaar').val(inst.selectedYear)

};

function setMyDate(inst) {
    var dateSetted = inst.input.data('date-setted');

    if (dateSetted == true) {
        var year = inst.input.data('year');
        var month = inst.input.data('month');
        var day = inst.input.data('day');

        var date = new Date(year, month, day);
        inst.input.datepicker('setDate', date );
    };
};

function formatDate(inst, date) {
    var formattedDate = $.datepicker.formatDate('MM yy', date);
    inst.input.val(formattedDate);
};