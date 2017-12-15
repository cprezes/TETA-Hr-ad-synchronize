#NoTrayIcon
#RequireAdmin
#Region ;**** Directives created by AutoIt3Wrapper_GUI ****
#AutoIt3Wrapper_Outfile=synchronize_ad.exe
#AutoIt3Wrapper_UseUpx=y
#AutoIt3Wrapper_UPX_Parameters=--ultra-brute
#AutoIt3Wrapper_Change2CUI=y
#AutoIt3Wrapper_Res_Comment=synchronize_ad
#AutoIt3Wrapper_Res_Description=synchronize_ad
#AutoIt3Wrapper_Res_Fileversion=1.2.1.48
#AutoIt3Wrapper_Res_Fileversion_AutoIncrement=y
#AutoIt3Wrapper_Res_LegalCopyright=Cezar z IT
#AutoIt3Wrapper_Res_Language=1045
#AutoIt3Wrapper_Res_Field=Cezar z IT|Cezar z IT
#AutoIt3Wrapper_Run_Tidy=y
#pragma compile(Console, True)
#EndRegion ;**** Directives created by AutoIt3Wrapper_GUI ****
#include <Constants.au3>
#include <GUIConstants.au3>
#include <Array.au3>
#include <File.au3>

Dim $ActiveXPosh
Const $OUTPUT_CONSOLE = 0
Const $OUTPUT_WINDOW = 1
Const $OUTPUT_BUFFER = 2

Global $sLogFile = @ScriptDir & "\log.txt"

Global $sFileOut = @ScriptDir & "\out.txt"
Global $sJavaRunPrg = @ScriptDir & "\Java\bin\javaw.exe -jar " & @ScriptDir & "\get.jar"

Global $iYearDay = 0

runUpdateDomanin()

While 1
	If $iYearDay <> @YDAY Then
		If @WDAY = 2 Then
			If @HOUR > 13 Then
				runUpdateDomanin()
			EndIf
		EndIf
	EndIf
	Sleep(60 * 60 * 1111)

WEnd



Func runUpdateDomanin()

	FileDelete($sFileOut)

	checkFileSize($sLogFile)

	_FileWriteLog($sLogFile, "=======Start Script======")

	Run(@ComSpec & " /C " & $sJavaRunPrg, @ScriptDir, @SW_HIDE)


	checkIfOutFileExist() ;~ if not exit program

	Local $aFileArray = 0

	If Not _FileReadToArray($sFileOut, $aFileArray, 0) Then Exit _FileWriteLog($sLogFile, "Dont find file out.txt, Check script get.jar")

	CreateActiveXPosh()

	$sPSCmd = 'import-module ActiveDirectory'
	execAndLog($sPSCmd)

	For $i = 0 To UBound($aFileArray) - 1
		execAndLog($aFileArray[$i] & @LF)
		Sleep(500)

	Next

	$ActiveXPosh = Null
	$iYearDay = @YDAY
EndFunc   ;==>runUpdateDomanin



Func execAndLog($sExec)
	Local $val = ExecuteCMD($sExec)
	_FileWriteLog($sLogFile, "Command => " & $sExec & @CRLF & ' Return =>  ' & $val)

EndFunc   ;==>execAndLog


Func CreateActiveXPosh()
	Local $success
	; Create the PowerShell connector object
	$ActiveXPosh = ObjCreate("SAPIEN.ActiveXPoSH")
	$success = $ActiveXPosh.Init(False) ;Do not load profiles
	If $success <> 0 Then
		_FileWriteLog($sLogFile, "SAPIEN.ActiveXPoSH Init failed")
		Exit 0
	EndIf
	If $ActiveXPosh.IsPowerShellInstalled Then
		_FileWriteLog($sLogFile, "SAPIEN.ActiveXPoSH Init OK, Ready to run PowerShell commands")
	Else
		_FileWriteLog($sLogFile, "PowerShell not installed")
		Exit 0
	EndIf
	; Set the output mode
	$ActiveXPosh.OutputMode = $OUTPUT_CONSOLE
	$ActiveXPosh.OutputWidth = 250
EndFunc   ;==>CreateActiveXPosh

Func ExecuteCMD($sPSCmd)
	Local $outtext = ''
	; Set the $OUTPUT mode to $BUFFER
	$ActiveXPosh.OutputMode = $OUTPUT_BUFFER
	$ActiveXPosh.Execute($sPSCmd)
	$outtext = $ActiveXPosh.OutputString
	$ActiveXPosh.ClearOutput()
	Return $outtext
EndFunc   ;==>ExecuteCMD



Func checkFileSize($sFile)
	Local $iFileSize = FileGetSize($sFile)
	If $iFileSize > 6313328 Then
		FileDelete($sFile)
		_FileWriteLog($sLogFile, "==========Clear log after 6MB===========")
	EndIf
EndFunc   ;==>checkFileSize

Func checkIfOutFileExist()

	Local $iSecWaitForFile = 30
	Local $bFileExist = False
	Local $i = 1

	While $i <= $iSecWaitForFile
		$i = $i + 1
		Sleep(1000)
		If FileExists($sFileOut) Then
			Sleep(3000)
			$i = $iSecWaitForFile + 1
			$bFileExist = True
		EndIf

		If $i <= $iSecWaitForFile And $bFileExist = False Then Exit _FileWriteLog($sLogFile,$i & " time I try and Don't find out.text, Check script get.jar, I go sleep for sec " &$iSecWaitForFile)


	WEnd
EndFunc   ;==>checkIfOutFileExist


