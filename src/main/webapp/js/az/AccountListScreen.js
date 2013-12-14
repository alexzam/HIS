Ext.define('alexzam.his.AccountListScreen', {
    extend:'Ext.container.Viewport',

    requires:[
        'alexzam.his.view.toolbar.Toolbar',
        'alexzam.his.model.manage.Accounts',

        'Ext.grid.Panel',
        'Ext.grid.column.Action',
        'Ext.layout.container.Anchor',
        'Ext.layout.container.Fit'
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

            bubbleEvents: ['accEdit'],

            items:[
                {
                    xtype:'grid',
                    title: 'Счета',
                    anchor: '50% 0',
                    margin: '5',

                    bubbleEvents: ['accEdit'],

                    tbar: [
                        {
                            xtype: 'button',
                            text: 'Новый счёт',
                            handler: function(){
                                this.fireEvent('accEdit', {caller:this, data: null});
                            },
                            icon: 'img/book_add.png',
                            bubbleEvents: ['accEdit']
                        }
                    ],

                    columns: [
                        {
                            text     : 'Название',
                            width     : 200,
                            dataIndex: 'name',
                            renderer:function(val, meta, record){
                                var icon = "";
                                if (!record.get('public'))
                                    icon = ' <span class="icon-private"></span>';
                                return val + icon;
                            }
                        },
                        {
                            text     : 'Сумма',
                            width    : 100,
                            dataIndex: 'val',
                            renderer: function(val) {
                                return Ext.util.Format.number(val / 100.0, "0.00");
                            }
                        },
                        {
                            xtype:'actioncolumn',
                            width:100,
                            bubbleEvents: ['accEdit'],
                            items: [
                                {
                                    icon: 'img/book_edit.png',
                                    tooltip: 'Редактировать',
                                    handler: function(grid, rowI, colI, el, ev, record){
                                        this.fireEvent('accEdit', {caller: this, data: record.data});
                                    }
                                }
                            ]
                        }
                    ]
                }
            ]
        }
    ],

    editaccDialog: Ext.create('Ext.window.Window',
        {
            title: 'Счёт',
            closeAction: 'hide',
            layout: 'fit',

            items:[
                {
                    xtype: 'form',

                    items:[
                        {
                            xtype:'textfield',
                            fieldLabel: 'Название',
                            name: 'name',
                            allowBlank: false
                        },
                        {
                            xtype:'radiogroup',
                            itemId: "rbPrivacy",
                            fieldLabel: 'Приватность',
                            vertical: true,
                            columns: 1,
                            defaults:{
                                name:'privacy'
                            },
                            items: [
                                {
                                    boxLabel: 'Общий',
                                    inputValue: 'C'
                                },
                                {
                                    boxLabel: 'Личный',
                                    inputValue: 'P'
                                },
                                {
                                    boxLabel: 'Скрытый',
                                    inputValue: 'H'
                                }
                            ]
                        }
                    ],

                    buttons:[
                        {text:"ОК"}
                    ]
                }
            ],

            setData: function(data){
                this.getComponent(0).getForm().setValues(data);
            }
        }
    ),

    listeners: {
        accEdit: function(param){
            this.editaccDialog.setData(param.data);
            this.editaccDialog.show(param.caller);
        }
    },

    initComponent:function () {
        var me = this;
        me.items[0].items[0].store = Ext.create('alexzam.his.model.manage.Accounts');

        me.callParent();
    }
});