Ext.define('alexzam.his.AccountListScreen', {
    extend:'Ext.container.Viewport',

    requires:[
        'alexzam.his.view.toolbar.Toolbar',
        'alexzam.his.model.manage.Accounts',

        'Ext.form.Panel',
        'Ext.form.RadioGroup',
        'Ext.grid.Panel',
        'Ext.grid.column.Action',
        'Ext.layout.container.Anchor',
        'Ext.layout.container.Fit',
        'Ext.window.Window'
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
                                this.fireEvent('accEdit', {
                                    caller:this,
                                    record: null
                                });
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
                                var privacy = record.get('privacy');
                                if (privacy == 'P')
                                    icon = ' <span class="icon-private"></span>';
                                if (privacy == 'H')
                                    icon = ' <span class="icon-hidden"></span>';
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
                                        this.fireEvent('accEdit', {caller: this, record: record});
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
            resizable: false,

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
                                    inputValue: 'C',
                                    checked: true
                                },
                                {
                                    boxLabel: 'Личный <span class="icon-private"></span>',
                                    inputValue: 'P'
                                },
                                {
                                    boxLabel: 'Скрытый <span class="icon-hidden"></span>',
                                    inputValue: 'H'
                                }
                            ]
                        }
                    ],

                    buttons:[
                        {
                            text:"ОК",
                            bubbleEvents: ['ok'],
                            handler:function(){
                                this.fireEvent('ok');
                            }
                        }
                    ],

                    bubbleEvents: ['ok']
                }
            ],

            record: null,
            screen: null,

            listeners: {
                ok: function(){
                    var formp = this.getComponent(0);
                    if (!formp.getForm().isValid()) return;

                    var data = formp.getValues();
                    if(this.record == null) {
                        this.record = Ext.create('alexzam.his.model.manage.model.Accounts');
                        formp.updateRecord(this.record);
                        this.screen.fireEvent('accAdd', {rec:this.record});
                    } else {
                        formp.updateRecord(this.record);
                    }

                    this.hide();
                }
            },

            setData: function(data){
                var form = this.getComponent(0).getForm();
                if(data == null) {
                    form.reset();
                    this.record = null;
                }
                else {
                    form.loadRecord(data);
                    this.record = data;
                }
            }
        }
    ),

    listeners: {
        accEdit: function(param){
            this.editaccDialog.setData(param.record);
            this.editaccDialog.show(param.caller);
        },

        accAdd:function(param){
            var grid = this.getComponent(0).getComponent(0);
            grid.getStore().add(param.rec);
        }
    },

    initComponent:function () {
        var me = this;
        me.items[0].items[0].store = Ext.create('alexzam.his.model.manage.Accounts');
        me.editaccDialog.screen = me;

        me.callParent();
    }
});