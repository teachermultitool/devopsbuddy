/**
 * Created by redmann on 12.10.16.
 */
$(document).ready(main);

function main() {
    $('.btn-collapse').click(function (e) {
        e.preventDefault();
        var $this = $(this);
        var $collapse = $this.closest('.collapse-group').find('.collapse');
        $collapse.collapse('toggle');
    });

}