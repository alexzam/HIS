Ext.define('alexzam.his.AccountListScreen', {
    extend:'Ext.container.Viewport',

    requires:[
        'alexzam.his.view.toolbar.Toolbar',

        'Ext.grid.Panel'
    ],

    items:[
        {
            xtype:'panel',
            layout:'border',
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
                    columns: [
                        {
                            text     : 'Название',
                            flex     : 1,
                            sortable : false,
                            dataIndex: 'name'
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