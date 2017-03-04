$(document).ready(function () {
    /*---------------------------- jQuery UI Datepicker ---------------------------------*/
    $("#Birthday").datepicker({
        changeMonth: true,
        changeYear: true,
        yearRange: "1920:2020"
    });
    $("#AOKBeginDate").datepicker({
        changeMonth: true,
        changeYear: true,
        yearRange: "2016:2020"
    });

    var LanguageCourseBegin = $("#LanguageCourseBegin")
        .datepicker({
            changeMonth: true,
            changeYear: true,
            yearRange: "2016:2020"
        })
        .on("change", function () {
            LanguageCourseEnd.datepicker("option", "minDate", getDate(this));
        }),
        LanguageCourseEnd = $("#LanguageCourseEnd").datepicker({
            changeMonth: true,
            changeYear: true,
            yearRange: "2016:2020"
        })
            .on("change", function () {
                LanguageCourseBegin.datepicker("option", "maxDate", getDate(this));
            }),
        KlemmerStudentBegin = $("#KlemmerStudentBegin")
            .datepicker({
                changeMonth: true,
                changeYear: true,
                yearRange: "2016:2020"
            })
            .on("change", function () {
                KlemmerStudentEnd.datepicker("option", "minDate", getDate(this));
            }),
        KlemmerStudentEnd = $("#KlemmerStudentEnd").datepicker({
            changeMonth: true,
            changeYear: true,
            yearRange: "2016:2020"
        })
            .on("change", function () {
                KlemmerStudentBegin.datepicker("option", "maxDate", getDate(this));
            });

    function getDate(element) {
        var date;
        try {
            date = $.datepicker.parseDate("yy-mm-dd", element.value);
        } catch (error) {
            date = null;
        }

        return date;
    }


    /*---------------------------- DateTable ---------------------------------*/
    $('.dataTables-partner').DataTable({
        pageLength: 25,
        responsive: true,
        dom: '<"html5buttons"B>lTfgitp',
        buttons: [
            { extend: 'csv' },
            { extend: 'excel', title: '保险合同' },
            { extend: 'pdf', title: '保险合同' },
            { extend: 'copy', text: '复制' },
            {
                extend: 'print',
                text: '打印',
                customize: function (win) {
                    $(win.document.body).addClass('white-bg');
                    $(win.document.body).css('font-size', '10px');

                    $(win.document.body).find('table')
                        .addClass('compact')
                        .css('font-size', 'inherit');
                }
            }
        ],
        language: {
            "url": "/partner-assets/js/plugins/dataTables/i18n/Chinese.json"
        }
    });
    $('.i-checks').iCheck({
        checkboxClass: 'icheckbox_square-green',
        radioClass: 'iradio_square-green',
    });
    $('table').on('click', '#getStudent', function () {
        var id = $(this).attr('data-id');
        $.getJSON("/partner/getStudentInfo", { id: id },
            function (data, textStatus, jqXHR) {
                $("#detailsModal #Sex").text(data.Sex || "");
                $("#detailsModal #Name").text(data.Name || "");
                $("#detailsModal #Address").text(data.Address || "");
                $("#detailsModal #ZIP").text(data.ZIP || "");
                $("#detailsModal #City").text(data.City || "");
                $("#detailsModal #Birthday").text(data.Birthday || "");
                $("#detailsModal #Birthplace").text(data.Birthplace || "");
                $("#detailsModal #Tel").text(data.Tel || "");
                $("#detailsModal #Email").text(data.Email || "");
                $("#detailsModal #AOK").text(data.AOK == "true" ? "是" : "否");
                if (data.AOK == "true") {
                    $("#detailsModal #AOKBeginDate").text(data.AOKBeginDate || "");
                    $("#detailsModal #UniName").text(data.UniName || "");
                    $("#detailsModal #StudienOrt").text(data.StudienOrt || "");
                    $("#detailsModal #Fachrichtung").text(data.Fachrichtung || "");
                    $("#detailsModal #Semesterbeginn").text(data.Semesterbeginn || "");
                } else {
                    $("#detailsModal #AOKBeginDate").text('-');
                    $("#detailsModal #UniName").text('-');
                    $("#detailsModal #StudienOrt").text('-');
                    $("#detailsModal #Fachrichtung").text('-');
                    $("#detailsModal #Semesterbeginn").text('-');
                }
                $("#detailsModal #LanguageCourse").text(data.LanguageCourse == "true" ? "是" : "否");
                if (data.LanguageCourse == "true") {
                    $("#detailsModal #LanguageCoursePlace").text(data.LanguageCoursePlace || "");
                    $("#detailsModal #LanguageCourseBegin").text(data.LanguageCourseBegin || "");
                    $("#detailsModal #LanguageCourseEnd").text(data.LanguageCourseEnd || "");
                } else {
                    $("#detailsModal #LanguageCoursePlace").text('-');
                    $("#detailsModal #LanguageCourseBegin").text('-');
                    $("#detailsModal #LanguageCourseEnd").text('-');
                }
                $("#detailsModal #KlemmerStudent").text(data.KlemmerStudent == "true" ? "是" : "否");
                if (data.KlemmerStudent == "true") {
                    $("#detailsModal #KlemmerStudentType").text(data.KlemmerStudentType || "");
                    $("#detailsModal #KlemmerStudentBegin").text(data.KlemmerStudentBegin || "");
                    $("#detailsModal #KlemmerStudentEnd").text(data.KlemmerStudentEnd || "");
                } else {
                    $("#detailsModal #KlemmerStudentType").text('-');
                    $("#detailsModal #KlemmerStudentBegin").text('-');
                    $("#detailsModal #KlemmerStudentEnd").text('-');
                }
                $('#detailsModal').modal('show')
            }
        );
    });
    $('table').on('click', '#editStudent', function () {
        var id = $(this).attr('data-id');
        $.getJSON("/partner/getStudentInfo", { id: id },
            function (data, textStatus, jqXHR) {
                $("#formModal .modal-title").text("修改 " + data.Name + " 同学的合同");
                if (data.Sex == "男") {
                    $("#formModal #male").prop('checked', true);
                    $("#formModal #female").prop('checked', false);
                } else {
                    $("#formModal #male").prop('checked', false);
                    $("#formModal #female").prop('checked', true);
                }
                $("#formModal #Name").val(data.Name || "");
                $("#formModal #Address").val(data.Address || "");
                $("#formModal #ZIP").val(data.ZIP || "");
                $("#formModal #City").val(data.City || "");
                $("#formModal #Birthday").val(data.Birthday || "");
                $("#formModal #Birthplace").val(data.Birthplace || "");
                $("#formModal #Tel").val(data.Tel || "");
                $("#formModal #Email").val(data.Email || "");
                if (data.AOK == "true") {
                    $("#formModal #AOK-yes").prop('checked', true);
                    $("#formModal #AOK-no").prop('checked', false);
                } else {
                    $("#formModal #AOK-yes").prop('checked', false);
                    $("#formModal #AOK-no").prop('checked', true);
                }
                $("#formModal #AOKBeginDate").val(data.AOKBeginDate || "");
                $("#formModal #UniName").val(data.UniName || "");
                $("#formModal #StudienOrt").val(data.StudienOrt || "");
                $("#formModal #Fachrichtung").val(data.Fachrichtung || "");
                $("#formModal #Semesterbeginn").val(data.Semesterbeginn || "");
                if (data.LanguageCourse == "true") {
                    $("#formModal #LanguageCourse-yes").prop('checked', true);
                    $("#formModal #LanguageCourse-no").prop('checked', false);
                } else {
                    $("#formModal #LanguageCourse-yes").prop('checked', false);
                    $("#formModal #LanguageCourse-no").prop('checked', true);
                }
                $("#formModal #LanguageCoursePlace").val(data.LanguageCoursePlace || "");
                $("#formModal #LanguageCourseBegin").val(data.LanguageCourseBegin || "");
                $("#formModal #LanguageCourseEnd").val(data.LanguageCourseEnd || "");
                if (data.KlemmerStudent == "true") {
                    $("#formModal #KlemmerStudent-yes").prop('checked', true);
                    $("#formModal #KlemmerStudent-no").prop('checked', false);
                } else {
                    $("#formModal #KlemmerStudent-yes").prop('checked', false);
                    $("#formModal #KlemmerStudent-no").prop('checked', true);
                }
                $("#formModal #KlemmerStudentType").val(data.KlemmerStudentType);
                $("#formModal #KlemmerStudentBegin").val(data.KlemmerStudentBegin || "");
                $("#formModal #KlemmerStudentEnd").val(data.KlemmerStudentEnd || "");
                $("#formModal form").attr("action", "/partner/editStudent?id=" + data._id);
                $('#formModal').modal('show')
            }
        );
    });
    $('#addStudent').click(function () {
        $("#formModal .modal-title").text("添加一个合同");
        $("#formModal #male").prop('checked', true);
        $("#formModal #female").prop('checked', false);
        $("#formModal #Name").val("");
        $("#formModal #Address").val("");
        $("#formModal #ZIP").val("");
        $("#formModal #City").val("");
        $("#formModal #Birthday").val("");
        $("#formModal #Birthplace").val("");
        $("#formModal #Tel").val("");
        $("#formModal #Email").val("");
        $("#formModal #AOK-yes").prop('checked', true);
        $("#formModal #AOK-no").prop('checked', false);
        $("#formModal #AOKBeginDate").val("");
        $("#formModal #UniName").val("");
        $("#formModal #StudienOrt").val("");
        $("#formModal #Fachrichtung").val("");
        $("#formModal #Semesterbeginn").val("");
        $("#formModal #LanguageCourse-yes").prop('checked', false);
        $("#formModal #LanguageCourse-no").prop('checked', true);
        $("#formModal #LanguageCoursePlace").val("");
        $("#formModal #LanguageCourseBegin").val("");
        $("#formModal #LanguageCourseEnd").val("");
        $("#formModal #KlemmerStudent-yes").prop('checked', false);
        $("#formModal #KlemmerStudent-no").prop('checked', true);
        $("#formModal #KlemmerStudentType").val("basic");
        $("#formModal #KlemmerStudentBegin").val("");
        $("#formModal #KlemmerStudentEnd").val("");
        $("#formModal form").attr("action", "/partner/addStudent/");
    });

    $('table').on('click', '#uploadAntrag', function () {
        var UID = $(this).attr('data-id');
        var studentID = $(this).attr('data-studentid');
        var studentName = $(this).attr('data-name');
        uploadImg(UID, studentID, "AntragPhoto");
        $("#uploadModal .modal-title").text("上传 " + studentName + " 同学已签名的用户信息表");
        $('#uploadModal').modal('show')
    });

    $('table').on('click', '#uploadPass', function () {
        var UID = $(this).attr('data-id');
        var studentID = $(this).attr('data-studentid');
        var studentName = $(this).attr('data-name');
        uploadImg(UID, studentID, "PassPhoto");
        $("#uploadModal .modal-title").text("上传 " + studentName + " 同学的护照照片");
        $('#uploadModal').modal('show')
    });

    function uploadImg(UID, studentID, filename) {
        var uploader = $("#uploader").fineUploader({
            request: {
                endpoint: '/upload/img?id=' + UID
            },
            multiple: false,
            autoUpload: false,
            camera: {
                ios: true
            },
            validation: {
                allowedExtensions: ['jpeg', 'jpg', 'gif', 'png', 'bmp'],
                sizeLimit: 10 * 1000 * 1000
            }
        }).on('error', function (event, id, name, reason) {
            // do something
        }).on('complete', function (event, id, name, responseJSON) {
            $('#uploadModal').on('hidden.bs.modal', function (e) {
                location.reload();
            })
        }).on('submitted', function (event, id, name) {
            uploader.fineUploader('setName', id, filename);
            uploader.fineUploader('setUuid', id, studentID);
        });
        uploader.on("click", "#upload_button", function () {
            uploader.fineUploader('uploadStoredFiles');
        });
    }
});