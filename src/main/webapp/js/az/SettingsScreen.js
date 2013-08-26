Ext.define('alexzam.his.SettingsScreen', {
    extend:'Ext.container.Viewport',

    requires:[
        'alexzam.his.view.toolbar.Toolbar',
        'Ext.layout.container.Border'
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
                    width:200,
                    bodyPadding: 5,
                    items:[
                        {
                            xtype: 'container',
                            border: 0,
                            html: "<p>Ну, настроек пока негусто. Одна!</p>"
                        },
                        {
                            xtype: 'fieldcontainer',
                            fieldLabel: 'Цветовая схема',
                            defaultType: 'radiofield',
                            layout: 'hbox',
                            items: [
                                {
                                    boxLabel: 'Классическая',
                                    name: 'colorScheme',
                                    inputValue: 'C'
                                },
                                {
                                    boxLabel: 'Серая',
                                    name: 'colorScheme',
                                    inputValue: 'G'
                                },
                                {
                                    boxLabel: 'Контрастная',
                                    name: 'colorScheme',
                                    inputValue: 'A'
                                }
                            ]
                        }
                    ]
                }
            ]
        }
    ]
});