Ext.define('alexzam.his.SettingsScreen', {
    extend:'Ext.container.Viewport',

    requires:[
        'alexzam.his.view.toolbar.Toolbar',
        'Ext.layout.container.Border',
        'Ext.form.RadioGroup',
        'Ext.form.field.Radio'
    ],

    layout:'fit',
    items:[
        {
            // Outer panel
            xtype:'panel',
            layout:'fit',
            dockedItems:[
                {
                    xtype:'his.Toolbar',
                    dock:'top',
                    disableId:'set'
                }
            ],
            items:[
                {
                    xtype:'panel',
                    region:'center',
                    bodyPadding: 5,
                    items:[
                        {
                            xtype: 'container',
                            border: 0,
                            html: "<p>Ну, настроек пока негусто. Одна!</p>"
                        },
                        {
                            xtype:'radiogroup',
                            itemId: "rdColors",
                            fieldLabel: 'Цветовая схема',
                            vertical:false,
                            defaults:{
                                width:100
                            },
                            items: [
                                {
                                    boxLabel: 'Классическая',
                                    name: 'colorScheme',
                                    inputValue: 'C',
                                    itemId: "colorC",
                                },
                                {
                                    boxLabel: 'Серая',
                                    name: 'colorScheme',
                                    inputValue: 'G',
                                    itemId: "colorG"
                                },
                                {
                                    boxLabel: 'Контрастная',
                                    name: 'colorScheme',
                                    inputValue: 'A',
                                    itemId: "colorA"
                                },
                                {
                                    boxLabel: 'Нептун',
                                    name: 'colorScheme',
                                    inputValue: 'N',
                                    itemId: "colorN"
                                }
                            ]
                        }
                    ]
                }
            ]
        }
    ],

    initComponent:function(){
        var me = this;
        me.callParent();

        var rdColors = me.getComponent(0).getComponent(0).getComponent('rdColors');
        rdColors.setValue({colorScheme:Ext.conf.params.colorScheme});

        rdColors.on('change', function(fld, newVal){
            document.location = Ext.conf.rootUrl + "settings/setColor?new=" + newVal.colorScheme;
        });
    }
});