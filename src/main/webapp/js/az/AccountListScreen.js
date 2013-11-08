Ext.define('accModel',{
    extend:'Ext.data.Model',
    fields: [
        {name: 'name', type: 'string'},
        {name: 'sum', type: 'string'}
    ]
});

Ext.define('alexzam.his.AccountListScreen', {
    extend:'Ext.container.Viewport',

    requires:[
        'alexzam.his.view.toolbar.Toolbar',

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
                                    icon: 'img/edit_button.png',
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

    store: Ext.create('Ext.data.Store', {
       model: 'accModel',
       data : [
               {name: 'Test',    sum: '5000'}
           ]
       }),

    initComponent:function () {
        var me = this;

        me.callParent();
    }
});