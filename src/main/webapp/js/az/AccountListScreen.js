Ext.define('alexzam.his.AccountListScreen', {
    extend:'Ext.container.Viewport',

    requires:[
        'alexzam.his.view.toolbar.Toolbar',
        'alexzam.his.model.manage.Accounts',

        'Ext.grid.Panel',
        'Ext.grid.column.Action',
        'Ext.layout.container.Anchor'
    ],
    layout:'fit',

    items:[
        {
            xtype:'panel',
            layout:'anchor',
            itemId:'pnlOuter',
            dockedItems:[{
                xtype:'his.Toolbar',
                dock:'top',
                disableId:'man'
            }],

            items:[
                {
                    xtype:'grid',
                    title: 'Счета',
                    anchor: '50% -10',
                    margin: '5',

                    columns: [
                        {
                            text     : 'Название',
                            width     : 200,
                            dataIndex: 'name'
                        },
                        {
                            text     : 'Сумма',
                            width    : 100,
                            dataIndex: 'sum'
                        },
                        {
                            xtype:'actioncolumn',
                            width:100,
                            items: [
                                {
                                    icon: 'img/book_edit.png',
                                    tooltip: 'Редактировать',
                                    handler: function(grid,rowI, colI){
                                        alert('edit-edit ' + rowI);
                                    }
                                }
                            ]
                        }
                    ]
                }
            ]
        }
    ],

    initComponent:function () {
        var me = this;
        me.items[0].items[0].store = Ext.create('alexzam.his.model.manage.Accounts');

        me.callParent();
    }
});