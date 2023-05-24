$(document).ready(function() {
    //function for auto-resizing of height of textarea
    function autoResizeTextarea() {
        $('textarea').css('height', 'auto');
        $('textarea').css('height', $('textarea')[0].scrollHeight + 'px');
    }

    //Use the function when loading the page and when changing the text in the textarea
    $('textarea').on('input', function() {
        autoResizeTextarea();
    });

    autoResizeTextarea(); //Use the function when loading the page
});