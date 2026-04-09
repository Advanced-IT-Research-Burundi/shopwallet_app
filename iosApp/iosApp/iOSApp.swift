import SwiftUI
import SharedWithYou
import ComposeApp

@main
struct iOSApp: App {
    
    init() {
        MainViewControllerKt.startKoinIOS()
    }
    
    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}