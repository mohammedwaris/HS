var config = {
    layout: {
        name: 'layout',
        padding: 0,
        panels: [
            { type: 'top', size: 32, content: '<div style="padding: 7px;">Top Panel</div>', style: 'border-bottom: 1px solid silver;' },
            { type: 'left', size: 200, resizable: true, minSize: 120 },
            { type: 'main', minSize: 350, overflow: 'hidden' }
        ]
    },
    sidebar: {
        name: 'sidebar',
        nodes: [ 
            { id: 'general', text: 'General', group: true, expanded: true, nodes: [
                { id: 'grid', text: 'Grid', img: 'icon-page', selected: true },
                { id: 'html', text: 'Some HTML', img: 'icon-page' }
            ]}
        ],
        onClick: function (event) {
            switch (event.target) {
                case 'grid':
                    w2ui.layout.content('main', w2ui.grid);
                    break;
                case 'html':
                    w2ui.layout.content('main', '<div style="padding: 10px">Some HTML</div>');
                    $(w2ui.layout.el('main'))
                        .removeClass('w2ui-grid')
                        .css({ 
                            'border-left': '1px solid silver'
                        });
                    break;
            }
        }
    },
    grid: { 
        name: 'grid',
        style: 'border: 0px; border-left: 1px solid silver',
        columns: [
            { field: 'state', caption: 'State', size: '80px' },
            { field: 'title', caption: 'Title', size: '100%' },
            { field: 'priority', caption: 'Priority', size: '80px', attr: 'align="center"' }
        ],
        records: [
            { recid: 1, state: 'Open', title: 'Short title for the record', priority: 2 },
            { recid: 2, state: 'Open', title: 'Short title for the record', priority: 3 },
            { recid: 3, state: 'Closed', title: 'Short title for the record', priority: 1 }
        ]
    }
};

$(function () {
    // initialization in memory
    $().w2layout(config.layout);
    $().w2sidebar(config.sidebar);
    $().w2grid(config.grid);
});

function openFileView() {
    w2popup.open({
        title   : 'Popup',
        width   : 800,
        height  : 600,
        showMax : true,
        body    : '<div id="main" style="position: absolute; left: 0px; top: 0px; right: 0px; bottom: 0px;"></div>',
        onOpen  : function (event) {
            event.onComplete = function () {
                $('#w2ui-popup #main').w2render('layout');
                w2ui.layout.content('left', w2ui.sidebar);
                w2ui.layout.content('main', w2ui.grid);
            }
        },
        onToggle: function (event) { 
            event.onComplete = function () {
                w2ui.layout.resize();
            }
        }        
    });
}