Ext.define('alexzam.his.AccountListScreen', {
    extend:'Ext.container.Viewport',

    requires:[
        'alexzam.his.view.toolbar.Toolbar',

        'Ext.grid.Panel'
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
                        }
                    ]
                }
            ]
        }
    ],

    initComponent:function () {
        var me = this;

        me.callParent();
    }
});